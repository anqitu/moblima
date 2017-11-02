package view;

import config.BookingConfig;
import exception.IllegalActionException;
import exception.RejectedNavigationException;
import exception.UnauthorisedNavigationException;
import manager.*;
import model.booking.Booking;
import model.booking.Showtime;
import model.booking.ShowtimeStatus;
import model.cinema.Cinema;
import model.cinema.Cineplex;
import model.commons.Language;
import model.movie.Movie;
import model.movie.MovieStatus;
import util.Utilities;
import view.ui.*;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class ShowtimeListView extends ListView {

    private Cineplex cineplexFilter;
    private Movie movieFilter;
    private Date dateFilter;
    private List<ShowtimeStatus> showtimeStatusFilters;
    private List<MovieStatus> movieStatusFilters;

    private CineplexController cineplexController;
    private MovieController movieController;
    private ShowtimeController showtimeController;
    private BookingController bookingController;
    private CinemaController cinemaController;

    private AccessLevel accessLevel;
    private ShowtimeListIntent intent;

    public ShowtimeListView(Navigation navigation) {
        super(navigation);
        cineplexController = CineplexController.getInstance();
        movieController = MovieController.getInstance();
        showtimeController = ShowtimeController.getInstance();
        bookingController = BookingController.getInstance();
        cinemaController = CinemaController.getInstance();
    }

    @Override
    public void onLoad(AccessLevel accessLevel, Intent intent, String... args) {

        // args[0] - Cineplex
        // args[1] - Movie
        // args[2] - Date

        if (args.length >= 1 && args[0] != null) {
            cineplexFilter = cineplexController.findById(UUID.fromString(args[0]));
            if (cineplexFilter == null) {
                View.displayError("Cineplex not found!");
                Form.pressAnyKeyToContinue();
                throw new RejectedNavigationException();
            }
        }

        if (args.length >= 2 && args[1] != null) {
            movieFilter = movieController.findById(UUID.fromString(args[1]));
            if (movieFilter == null) {
                View.displayError("Movie not found!");
                Form.pressAnyKeyToContinue();
                throw new RejectedNavigationException();
            }
        }

        if (args.length == 3 && args[2] != null) {
            try {
                dateFilter = Utilities.parseDateIgnoreError(args[2]);
            } catch (ParseException e) {
                View.displayError("Invalid date!");
                Form.pressAnyKeyToContinue();
                throw new RejectedNavigationException();
            }
        } else
            dateFilter = new Date();

        this.accessLevel = accessLevel;
        switch (accessLevel) {
            case ADMINISTRATOR:
                if (cineplexFilter != null && movieFilter != null)
                    setMenuItems(ShowtimeListOption.values());
                else
                    setMenuItems(ShowtimeListOption.CHOOSE_DAY);
                break;
            case PUBLIC:
                setMenuItems(ShowtimeListOption.CHOOSE_DAY);
                showtimeStatusFilters = Arrays.asList(ShowtimeStatus.OPEN_BOOKING);
                movieStatusFilters = Arrays.asList(MovieStatus.NOW_SHOWING,
                        MovieStatus.COMING_SOON,
                        MovieStatus.PREVIEW);
                break;
        }

        this.intent = (ShowtimeListIntent) intent;
        switch (this.intent) {
            case VIEW_SHOWTIMES:
                break;
            case CREATE_SHOWTIME:
                if (accessLevel != AccessLevel.ADMINISTRATOR)
                    throw new UnauthorisedNavigationException();

                View.displayInformation("Please enter showtime details.");
                List<Cinema> cinemas = cineplexFilter.getCinemas();;

                Cinema cinema = cinemaController.findById(UUID.fromString(Form.getOption("Cinema",
                        cinemas.stream().map(cineplexCinema ->
                        new GenericMenuOption("Hall " + cineplexCinema.getCode(),
                                cineplexCinema.getId().toString())).toArray(GenericMenuOption[]::new))));
                Language language = Language.valueOf(Form.getOption("Language", Language.values()));
                int numberOfSubtitles = Form.getIntWithMin("Number of Subtitles", 0);
                Language[] subtitles = new Language[numberOfSubtitles];
                for (int i = 0; i < numberOfSubtitles; i++)
                    subtitles[i] = Language.valueOf(Form.getOption("Subtitle " + (i + 1), Language.values()));

                // Get today's date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
                int year = calendar.get(Calendar.YEAR);

                // Set date to specified hour and minute
                Date startTime = Form.getDate("Start Time", "HH:mm");
                calendar.setTime(startTime);
                calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
                calendar.set(Calendar.YEAR, year);
                startTime = calendar.getTime();

                boolean isPreview = Form.getBoolean("Preview Movie");
                boolean noFreePasses = Form.getBoolean("No Free Passes");
                showtimeController.createShowtime(movieFilter.getId(), cineplexFilter.getId(), cinema.getId(),
                        language, startTime, isPreview, noFreePasses, subtitles);
                View.displaySuccess("Successfully created showtime!");
                Form.pressAnyKeyToContinue();
                break;
        }

        setTitle("Movie Showtimes");
        addBackOption();
    }

    @Override
    public void onEnter() {

        setContent("Movie: " + (movieFilter == null ? "All Movies" : movieFilter.toString()),
                "Cineplex: " + (cineplexFilter == null ? "All Cineplexes" : cineplexFilter.getName()),
                "Date: " + Utilities.toFormat(dateFilter, DATE_DISPLAY_FORMAT));

        ArrayList<ViewItem> viewItems = new ArrayList<>();
        if (cineplexFilter == null) {

            List<Cineplex> cineplexes = cineplexController.getList();
            for (Cineplex cineplex : cineplexes) {
                List<Movie> cineplexMovies = movieController.findByCineplex(cineplex.getId());
                if (movieFilter != null && cineplexMovies.contains(movieFilter))
                    cineplexMovies = Arrays.asList(movieFilter);
                else if (movieFilter != null)
                    cineplexMovies = new ArrayList<>();

                if (movieStatusFilters != null)
                    cineplexMovies = cineplexMovies.stream().filter(movie ->
                            movieStatusFilters.contains(movie.getStatus())).collect(Collectors.toList());

                ArrayList<String> content = new ArrayList<>();
                for (Movie movie : cineplexMovies) {
                    List<Showtime> movieShowtimes = showtimeController
                            .findByCineplexAndMovie(cineplex.getId(), movie.getId())
                            .stream().filter(showtime -> Utilities.isOnSameDay(dateFilter, showtime.getStartTime()))
                            .collect(Collectors.toList());
                    if (showtimeStatusFilters != null)
                        movieShowtimes = movieShowtimes.stream().filter(showtime ->
                                showtimeStatusFilters.contains(showtime.getStatus())).collect(Collectors.toList());
                    content.add(new MovieView(movie, movieShowtimes).flatten(" : ", " "));
                }

                if (content.size() == 0)
                    content.add("No available showtime screenings for this cineplex");

                viewItems.add(new ViewItem(cineplex.getName(), content, cineplex.getId().toString()));
            }

        } else if (movieFilter == null) {

            List<Movie> cineplexMovies = accessLevel == AccessLevel.ADMINISTRATOR ?
                    movieController.getList() :
                    movieController.findByCineplex(cineplexFilter.getId());

            if (movieStatusFilters != null)
                cineplexMovies = cineplexMovies.stream().filter(movie ->
                        movieStatusFilters.contains(movie.getStatus())).collect(Collectors.toList());

            for (Movie movie : cineplexMovies) {
                List<Showtime> movieShowtimes = showtimeController
                        .findByCineplexAndMovie(cineplexFilter.getId(), movie.getId())
                        .stream().filter(showtime -> Utilities.isOnSameDay(dateFilter, showtime.getStartTime()))
                        .collect(Collectors.toList());

                if (showtimeStatusFilters != null)
                    movieShowtimes = movieShowtimes.stream().filter(showtime ->
                            showtimeStatusFilters.contains(showtime.getStatus())).collect(Collectors.toList());

                viewItems.add(new ViewItem(new MovieView(movie, movieShowtimes), movie.getId().toString()));
            }

        } else {

            List<Showtime> showtimes = showtimeController
                    .findByCineplexAndMovie(cineplexFilter.getId(), movieFilter.getId())
                    .stream().filter(showtime -> Utilities.isOnSameDay(dateFilter, showtime.getStartTime()))
                    .collect(Collectors.toList());

            if (showtimeStatusFilters != null)
                showtimes = showtimes.stream().filter(showtime ->
                        showtimeStatusFilters.contains(showtime.getStatus())).collect(Collectors.toList());

            for (Showtime showtime : showtimes)
                viewItems.add(new ViewItem(new ShowtimeView(showtime), showtime.getId().toString()));
        }

        setViewItems(viewItems);

        display();
        View.displayInformation("Please select ");
        String userInput = getChoice();
        if (userInput.equals(BACK))
            navigation.goBack();
        else
            try {
                ShowtimeListOption userChoice = ShowtimeListOption.valueOf(userInput);
                switch (userChoice) {
                    case CHOOSE_DAY:
                        View.displayInformation("Please select date");
                        Date today = new Date();
                        // Get user date selection from today to number of days before booking
                        String dateChoice = Form.getOption("Date", Utilities.getDaysBetweenDates(today,
                                Utilities.getDateAfter(today, Calendar.DAY_OF_YEAR,
                                        BookingConfig.getDaysBeforeOpenBooking())).stream()
                                .filter(date -> !Utilities.isOnSameDay(date, dateFilter))
                                .map(date -> new GenericMenuOption(Utilities.toFormat(date, DATE_DISPLAY_FORMAT),
                                        Utilities.toFormat(date))).toArray(GenericMenuOption[]::new));
                        dateFilter = Utilities.parseDate(dateChoice);
                        navigation.refresh();
                        break;
                    case ADD_SHOWTIME:
                        navigation.reload(accessLevel, ShowtimeListIntent.CREATE_SHOWTIME,
                                cineplexFilter.getId().toString(),
                                movieFilter.getId().toString(),
                                Utilities.toFormat(dateFilter));
                        break;
                }
            } catch (IllegalArgumentException e) {
                if (cineplexFilter == null) {
                    navigation.goTo(new ShowtimeListView(navigation), accessLevel,
                            ShowtimeListIntent.VIEW_SHOWTIMES,
                            userInput,
                            movieFilter == null ? null : movieFilter.getId().toString(),
                            Utilities.toFormat(dateFilter));
                } else if (movieFilter == null) {
                    navigation.goTo(new ShowtimeListView(navigation), accessLevel,
                            ShowtimeListIntent.VIEW_SHOWTIMES,
                            cineplexFilter == null ? null : cineplexFilter.getId().toString(),
                            userInput,
                            Utilities.toFormat(dateFilter));
                } else {
                    if (accessLevel == AccessLevel.PUBLIC) {
                        try {
                            Booking booking = bookingController.createBooking(UUID.fromString(userInput));
                            navigation.goTo(new TicketTypeListView(navigation), accessLevel, booking.getId().toString());
                        } catch (IllegalActionException ex) {
                            View.displayError("Sorry, cannot book for this showtime at this time.");
                            navigation.refresh();
                        }
                    } else {
                        navigation.goTo(new TicketTypeListView(navigation), accessLevel, userInput);
                    }
                }
            }

    }

    public enum ShowtimeListIntent implements Intent {
        VIEW_SHOWTIMES,
        CREATE_SHOWTIME
    }

    public enum ShowtimeListOption implements EnumerableMenuOption {

        CHOOSE_DAY("Choose Another Date"),
        ADD_SHOWTIME("Add Showtime");

        private String description;

        ShowtimeListOption(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }
}

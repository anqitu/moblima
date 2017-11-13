package controller;

import exception.IllegalActionException;
import exception.UninitialisedSingletonException;
import model.booking.Booking;
import model.booking.BookingStatus;
import model.booking.Showtime;
import model.booking.ShowtimeStatus;
import model.cineplex.Cineplex;
import model.commons.Searchable;
import model.movie.*;
import util.Utilities;

import java.util.*;
import java.util.stream.Collectors;

/**
 Represents the moblima.controller of movies.
 @version 1.0
 @since 2017-10-20
 */
public class MovieController extends EntityController<Movie> {

    /**
     * A reference to this singleton instance.
     */
    private static MovieController instance;

    /**
     * Creates the movie moblima.controller.
     */
    private MovieController() { super(); }

    /**
     * Initialize the movie moblima.controller.
     */
    public static void init() { instance = new MovieController(); }

    /**
     * Gets this Movie Controller's singleton instance.
     * @return this Movie Controller's singleton instance.
     */
    public static MovieController getInstance() {
        if (instance == null)
            throw new UninitialisedSingletonException();
        return instance;
    }

    /**
     * Creates a movie with the given movie information and put it into entities.
     * @param title The title of this movie.
     * @param synopsis The synopsis of this movie.
     * @param director The director of this movie.
     * @param actors The actors of this movie.
     * @param movieType The movieType of this movie.
     * @param status The status of this movie.
     * @param rating The rating of this movie.
     * @param runtimeMinutes The runtime in minutes of this movie.
     * @return The newly created movie.
     */
    public Movie createMovie(String title, String synopsis, MoviePerson director,
                             MoviePerson[] actors, MovieType movieType,
                             MovieStatus status, MovieRating rating, int runtimeMinutes) {
        Movie movie = new Movie(title, synopsis, director, movieType, actors, status, rating, runtimeMinutes);
        entities.put(movie.getId(), movie);
        return movie;
    }

    /**
     * Changes the status of the movie with the given movie ID.
     * @param movieId The ID of the movie whose status is to be changed.
     * @param status The new status of this movie.
     * @throws IllegalActionException if the change of the movie status is illegal
     * according to business rule. The movie can not end showing if it is still open for booking.
     * The movie can not be set to preview if it is not coming soon now. The movie can not
     * be changed to showing now is it ends showing now. The movie can not be changed to coming soon
     * if it is in other status now.
     */
    public void changeMovieStatus(UUID movieId, MovieStatus status) throws IllegalActionException {

        Movie movie = findById(movieId);
        if (movie.getStatus() == status)
            throw new IllegalActionException("Already in '" + status + "' state.");

        switch (status) {
            case END_OF_SHOWING:
                for (Showtime showtime : movie.getShowtimes())
                    if (showtime.getStatus() == ShowtimeStatus.OPEN_BOOKING)
                        throw new IllegalActionException("Cannot remove movies with upcoming/ongoing showtimes.");
                break;
            case PREVIEW:
                if (movie.getStatus() != MovieStatus.COMING_SOON)
                    throw new IllegalActionException("Can only change movie status to 'Preview' " +
                            "if it is 'Coming Soon'.");
                break;
            case NOW_SHOWING:
                if (movie.getStatus() == MovieStatus.END_OF_SHOWING)
                    throw new IllegalActionException("Cannot change movie status to 'Now Showing' " +
                            "because it is 'End of Showing' now.");
                break;
            case COMING_SOON:
                throw new IllegalActionException("Can not change movie status to 'Coming Soon'.");
        }

        movie.setStatus(status);
    }

    /**
     * Change the movie details of the movie with the given movie ID.
     * @param movieId The ID of the movie to be changed.
     * @param title The new title of this movie.
     * @param synopsis The new synopsis of this movie.
     * @param director The new director of this movie.
     * @param actors The new actors of this movie.
     * @param rating The new rating of this movie.
     * @param runtimeMinutes The new runtimeMinutes in minutes of this movie.
     */
    public void changeMovieDetails(UUID movieId, String title, String synopsis,
                                   MoviePerson director, MoviePerson[] actors,
                                   MovieRating rating, int runtimeMinutes) {
        Movie movie = findById(movieId);
        movie.setTitle(title);
        movie.setSynopsis(synopsis);
        movie.setDirector(director);
        movie.setActors(actors);
        movie.setRating(rating);
        movie.setRuntime(runtimeMinutes);
    }

    /**
     * Change the movie type of the movie with the given ID to the given movie type.
     * @param movieId The ID of the movie to be changed.
     * @param type The new movie type of this movie.
     * @throws IllegalActionException if the movie to be changes is not coming soon now, which means
     * it is showing or has ended showing already.
     */
    public void changeMovieType(UUID movieId, MovieType type) throws IllegalActionException {
        Movie movie = findById(movieId);
        if (movie.getStatus() != MovieStatus.COMING_SOON)
            throw new IllegalActionException("Cannot only change movie type when it is not yet available for screening");
        movie.setType(type);
    }

    /**
     * Finds movies by the given list of movie status.
     * @param status The list of movie status of the movies to be searched for.
     * @return a list of movies in the status inside this list of movie status.
     */
    public List<Movie> findByStatus(MovieStatus... status) {
        ArrayList<Movie> movies = new ArrayList<>();
        List<MovieStatus> statuses = Arrays.asList(status);
        for (Movie movie : entities.values())
            if (statuses.contains(movie.getStatus()))
                movies.add(movie);
        return movies;
    }

    /**
     * Finds movies by the given keyword.
     * @param keyword The keyword to be searched for.
     * @return a list of movies that has the information of which similarity to the keyword
     * is within the similarity threshold.
     */
    @SuppressWarnings("unchecked")
    public List<Movie> findByKeyword(String keyword) {
        return Searchable.fuzzySearch(entities.values().stream()
                .map(movie -> (Searchable) movie)
                .collect(Collectors.toList()), keyword)
                .stream().map(searchable -> (Movie) searchable)
                .collect(Collectors.toList());
    }

    /**
     * Finds all the movies shown in a given cineplex.
     *
     * @param cineplexId The ID of the cineplex to be searched for.
     * @return all the movies shown in this cineplex.
     */
    public List<Movie> findByCineplex(UUID cineplexId) {
        CineplexController cineplexController = CineplexController.getInstance();
        Cineplex cineplex = cineplexController.findById(cineplexId);
        return entities.values().stream().filter(movie ->
                movie.getShowtimes().stream().anyMatch(showtime ->
                        showtime.getCineplex().equals(cineplex))).collect(Collectors.toList());
    }

    /**
     * Gets the total ticket sale of a movie with the given movie ID.
     *
     * @param movieId the given ID of a movie.
     * @return this movie's total ticket sale.
     */
    public int getTicketSales(UUID movieId) {
        Movie movie = findById(movieId);
        int sum = 0;
        for (Showtime showtime : movie.getShowtimes())
            if (showtime.getStatus() != ShowtimeStatus.CANCELLED)
                for (Booking booking : showtime.getBookings())
                    if (booking.getStatus() == BookingStatus.CONFIRMED)
                        sum += booking.getTotalTicketsCount();
        return sum;
    }
}

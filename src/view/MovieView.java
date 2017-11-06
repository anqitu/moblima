package view;

import model.booking.Showtime;
import model.movie.Movie;
import util.Utilities;
import view.ui.View;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This view displays the user interface for the user to view movie details.
 *
 * @version 1.0
 * @since 2017-10-30
 */

public class MovieView extends View {


    public MovieView(Movie movie) {
        double rating = movie.getOverallReviewRating();

        setTitle(movie.toString());
        setContent("Director: " + movie.getDirector(),
                "Actors: " + String.join(",",
                        movie.getActors().stream().map(String::valueOf).collect(Collectors.toList())),
                "Runtime: " + movie.getRuntimeMinutes() + " minutes",
                "Score: " + (rating == -1 ? "NA" : String.format("%.1f/5.0", rating)),
                " ",
                "Sypnosis",
                "--------",
                movie.getSynopsis());
    }

    public MovieView(Movie movie, List<Showtime> showtimes) {
        setTitle(movie.toString());
        if (showtimes.size() > 0) {
            Collections.sort(showtimes);
            setContent(showtimes.stream().map(showtime ->
                    Utilities.toFormat(showtime.getStartTime(), "[hh:mm a]")).toArray(String[]::new));
        } else
            setContent("No available showtime screenings for this movie.");

    }
}

package model.movie;

import model.booking.*;
import model.commons.Entity;
import model.commons.Searchable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a movie.
 *
 * @version 1.0
 * @since 2017-10-20
 */
public class Movie extends Entity implements Searchable {

    /**
     * The title of this movie.
     */
    private String title;

    /**
     * The synopsis of this movie.
     */
    private String synopsis;

    /**
     * The directer of this movie.
     */
    private MoviePerson director;

    /**
     * The type of this movie.
     */
    private MovieType type;

    /**
     * The array list of actors of this movie.
     */
    private ArrayList<MoviePerson> actors;

    /**
     * The array list of reviews of this movie.
     */
    private ArrayList<MovieReview> reviews;

    /**
     * The array list of showtimes of this movie.
     */
    private ArrayList<Showtime> showtimes;

    /**
     * The status of this movie.
     */
    private MovieStatus status;

    /**
     * The rating of this movie.
     */
    private MovieRating rating;

    /**
     * The runtime of this movie in minutes.
     */
    private int runtimeMinutes;

    /**
     * Creates a moview with the given titles, synopsis, director, type, actors, status, rating and runtime in minutes.
     *
     * @param title          The title of this movie.
     * @param synopsis       The synopsis of this movie.
     * @param director       The directer of this movie.
     * @param type           The type of this movie.
     * @param actors         The array of actors of this movie.
     * @param status         The status of this movie.
     * @param rating         The rating of this movie.
     * @param runtimeMinutes The runtime of this movie in minutes.
     */
    public Movie(String title, String synopsis, MoviePerson director, MovieType type,
                 MoviePerson[] actors, MovieStatus status, MovieRating rating, int runtimeMinutes) {
        this.title = title;
        this.synopsis = synopsis;
        this.director = director;
        this.actors = new ArrayList<MoviePerson>(Arrays.asList(actors));
        this.type = type;
        this.reviews = new ArrayList<MovieReview>();
        this.showtimes = new ArrayList<Showtime>();
        this.status = status;
        this.rating = rating;
        this.runtimeMinutes = runtimeMinutes;
    }

    /**
     * Gets this movie's title.
     *
     * @return this movie's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Changes this movie's title.
     *
     * @param title The new title of this movie.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets this movie's synopsis.
     *
     * @return this movie's synopsis.
     */
    public String getSynopsis() {
        return synopsis;
    }

    /**
     * Changes this movie's synopsis.
     *
     * @param synopsis The new synopsis of this movie.
     */
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    /**
     * Gets this movie's director.
     *
     * @return this movie's director.
     */
    public MoviePerson getDirector() {
        return director;
    }

    /**
     * Changes this movie's director.
     *
     * @param director The new director of this movie.
     */
    public void setDirector(MoviePerson director) {
        this.director = director;
    }

    /**
     * Gets this movie's type.
     *
     * @return this movie's type.
     */
    public MovieType getType() {
        return type;
    }

    /**
     * Changes this movie's type.
     *
     * @param type The new type of this movie.
     */
    public void setType(MovieType type) {
        this.type = type;
    }

    /**
     * Gets this movie's list of actors.
     *
     * @return this movie's list of actors.
     */
    public List<MoviePerson> getActors() {
        return actors;
    }

    /**
     * Changes this movie's actors.
     *
     * @param actors The new actors list of this movie.
     */
    public void setActors(MoviePerson[] actors) {
        this.actors = new ArrayList<MoviePerson>(Arrays.asList(actors));
    }

    /**
     * Gets this movie's list of reviews.
     *
     * @return this movie's list of reviews.
     */
    public List<MovieReview> getReviews() {
        return reviews;
    }

    /**
     * Gets this movie's list of showtime.
     *
     * @return this movie's list of showtime.
     */
    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    /**
     * Gets this movie's status.
     *
     * @return this movie's status.
     */
    public MovieStatus getStatus() {
        return status;
    }

    /**
     * Changes this movie's status.
     *
     * @param status The new status of this movie.
     */
    public void setStatus(MovieStatus status) {
        this.status = status;
    }

    /**
     * Gets this movie's rating.
     *
     * @return this movie's rating.
     */
    public MovieRating getRating() {
        return rating;
    }

    /**
     * Changes this movie's rating.
     *
     * @param rating The new rating of this movie.
     */
    public void setRating(MovieRating rating) {
        this.rating = rating;
    }

    /**
     * Gets this movie's runtime in minutes.
     *
     * @return this movie's runtime in minutes.
     */
    public int getRuntimeMinutes() {
        return runtimeMinutes;
    }

    /**
     * Gets this movie's search tag which can be its title, director or actors.
     *
     * @return this movie's search tag.
     */
    public List<String> getSearchTags() {

        ArrayList<String> tags = new ArrayList<>();

        // Movie Title
        tags.addAll(Arrays.asList(title.split(" ")));
        tags.add(title);

        // Actors and Director
        for (MoviePerson actor : actors)
            tags.addAll(actor.getSearchTags());
        tags.addAll(director.getSearchTags());
        return tags;
    }

    /**
     * Adds a review to this movie.
     *
     * @param movieReview The review to be added to this movie.
     */
    public void addReview(MovieReview movieReview) {
        reviews.add(movieReview);
    }

    /**
     * Removes a reviwe from this movie.
     *
     * @param movieReview The review to be removed from this movie.
     */
    public void removeReview(MovieReview movieReview) {
        reviews.remove(movieReview);
    }

    /**
     * Adds a showtime to this movie.
     *
     * @param showtime The showtime to be added to this movie.
     */
    public void addShowtime(Showtime showtime) {
        showtimes.add(showtime);
    }

    /**
     * Removes a showtime from this movie.
     *
     * @param showtime The showtime to be removed this movie.
     */
    public void removeShowtime(Showtime showtime) {
        showtimes.remove(showtime);
    }

    /**
     * Changes this movie's runtime in minutes.
     *
     * @param runtimeMinutes The new runtime in minutes of this movie.
     */
    public void setRuntime(int runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }

    /**
     * Gets the overall rating of this movie.
     *
     * @return this movie's overall rating.
     */
    public double getOverallReviewRating() {
        if (getReviews().size() <= 1)
            return -1;
        int sum = getReviews().stream().map(MovieReview::getRating).mapToInt(Integer::intValue).sum();
        return (double) sum / getReviews().size();
    }

    /**
     * Gets the gross sales of this movie.
     * @return the gross sales of this movie.
     */
    public double getGrossSales() {
        double gross = 0;
        for (Showtime showtime : showtimes)
            if (showtime.getStatus() != ShowtimeStatus.CANCELLED)
                for (Booking booking : showtime.getBookings())
                    if (booking.getStatus() == BookingStatus.CONFIRMED)
                        gross += booking.getPrice();
        return gross;
    }

    /**
     * Gets the weekend's gross sales of this movie.
     * @return the weekend's gross sales of this movie.
     */
    public double getWeekendGrossSales() {
        double weekendGross = 0;
        for (Showtime showtime : showtimes)
            if (showtime.getStatus() != ShowtimeStatus.CANCELLED && TicketType.isPeak(showtime.getStartTime()))
                for (Booking booking : showtime.getBookings())
                    if (booking.getStatus() == BookingStatus.CONFIRMED)
                        weekendGross += booking.getPrice();
        return weekendGross;
    }

    /**
     * Gets the title, type and rating this movie as a string.
     *
     * @return the title, type and rating this movie as a string.
     */
    @Override
    public String toString() {
        return String.format("%s [%s] %s", title, type, rating);
    }

    /**
     * Gets the title, type and rating this movie as a string
     * with or without "*" to indicate whether there is free pass.
     * @param noFreePasses whether there is free pass.
     * @return the title, type and rating this movie as a string
     * with or without "*" to indicate whether there is free pass.
     */
    public String toString(boolean noFreePasses) {
        return (noFreePasses ? "*" : "") + String.format("%s [%s] %s", title, type, rating);
    }

}


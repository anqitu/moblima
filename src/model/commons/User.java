package model.commons;

import model.booking.Booking;
import model.booking.BookingStatus;
import model.movie.Movie;
import model.movie.MovieReview;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user of the application, who is the moviegoers that can book tickets and write reviews.
 *
 * @version 1.0
 * @since 2017-10-20
 */
public class User extends Person {

    /**
     * This user's mobile number.
     */
    private String mobile;

    /**
     * This user's email address.
     */
    private String email;

    /**
     * An array list of movie reviews written by this user.
     * An user can write zero or many reviews.
     * A user can only write movie reviews for the movie he or she already booked and watched.
     */
    private ArrayList<MovieReview> reviews;

    /**
     * An array list of bookings made by this user.
     * An user must have at least one booking.
     */
    private ArrayList<Booking> bookings;

    /**
     * Creates an user with the first name, last name, mobile number and email address.
     *
     * @param firstName This user's first name.
     * @param lastName  This user's last name.
     * @param mobile    This user's mobile number.
     * @param email     This user's email address.
     */
    public User(String firstName, String lastName, String mobile, String email) {
        super(firstName, lastName);
        this.mobile = mobile;
        this.email = email;
        this.reviews = new ArrayList<MovieReview>();
        this.bookings = new ArrayList<Booking>();
    }

    /**
     * Gets this user's mobile number.
     *
     * @return this user's mobile number.
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Changes this user's mobile number.
     *
     * @param mobile This user's new mobile number.
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets this user's email address.
     *
     * @return this user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Changes this user's email address.
     *
     * @param email This user's new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the list of movie reviews written by this user.
     *
     * @return the list of movie reviews written by this user.
     */
    public List<MovieReview> getMovieReviews() {
        return reviews;
    }

    /**
     * Gets the list of bookings made by this user.
     *
     * @return the list of bookings made by this user.
     */
    public List<Booking> getBookings() {
        return bookings;
    }

    /**
     * Add a new movie review written by the user to the array list of movie reviews.
     *
     * @param movieReview A new movie review written by the user.
     */
    public void addReview(MovieReview movieReview) {
        reviews.add(movieReview);
    }

    /**
     * Remove a movie review written by the user from the array list of movie reviews.
     *
     * @param movieReview the movie review to be removed.
     */
    public void removeReview(MovieReview movieReview) {
        reviews.remove(movieReview);
    }

    /**
     * Add a new booking booked by the user to the array list of bookings.
     *
     * @param booking The booking to be added to this user.
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    /**
     * Remove a booking booked by the user from the array list of bookings.
     *
     * @param booking The booing to be removed from this user.
     */
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    public boolean hasBooking(Movie movie) {
        for (Booking booking : bookings)
            if (booking.getShowtime().getMovie().equals(movie) && booking.getStatus() == BookingStatus.CONFIRMED)
                return true;
        return false;
    }

    public boolean hasMovieReview(Movie movie) {
        for (MovieReview movieReview : reviews)
            if (movieReview.getMovie().equals(movie))
                return true;
        return false;
    }
}

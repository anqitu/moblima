package controller;

import config.BookingConfig;
import exception.IllegalActionException;
import exception.UninitialisedSingletonException;
import model.booking.*;
import model.cineplex.Seat;
import model.commons.User;
import model.transaction.Payment;
import model.transaction.PaymentStatus;

import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

/**
 Represents the moblima.controller of booking.
 @version 1.0
 @since 2017-10-20
 */
public class BookingController extends EntityController<Booking> {

    /**
     * A reference to this singleton instance.
     */
    private static BookingController instance;

    /**
     * Creates the Booking Controller.
     */
    private BookingController() {
        super();
    }

    /**
     * Initialize the Booking Controller.
     */
    public static void init() {
        instance = new BookingController();
    }

    /**
     * Gets this Booking Controller's singleton instance.
     * @return this Booking Controller's singleton instance.
     */
    public static BookingController getInstance() {
        if (instance == null)
            throw new UninitialisedSingletonException();
        return instance;
    }

    /**
     * Creates a booking when the user comes to the booking moblima.view after choosing the showtime.
     * A booking can only be created for a showtime that is open for booking at that moment.
     * The booking will be put into the entities.
     * @param showtimeId The ID of the showtime choosen by the user.
     * @return The newly created booking for this showtime.
     * @throws IllegalActionException if the showtime is not yet open for booking.
     */
    public Booking createBooking(UUID showtimeId) throws IllegalActionException {

        ShowtimeController showtimeController = ShowtimeController.getInstance();
        Showtime showtime = showtimeController.findById(showtimeId);

        // Check whether showtime is open for booking
        if (showtime.getStatus() != ShowtimeStatus.OPEN_BOOKING)
            throw new IllegalActionException("Can only book when the movie is open for booking");

        // Create the booking
        Booking booking = new Booking(showtime);

        // Add booking to entities
        entities.put(booking.getId(), booking);

        return booking;
    }


    /**
     * Creates tickets for a booking when the user selects ticketTypes, adding the tickets to the booking.
     * @param bookingId The ID of the given booking.
     * @param ticketTypesCount The total number of tickets.
     * @throws IllegalActionException if the number of tickets exceeds the allowed maximum number of
     * tickets to book, or if the ticket type is not available for this ticket type,
     * or if the booking is not in progress.
     */
    public void selectTicketType(UUID bookingId, Hashtable<TicketType, Integer> ticketTypesCount)
            throws IllegalActionException {

        ShowtimeController showtimeController = ShowtimeController.getInstance();

        Booking booking = findById(bookingId);

        // Check if booking not in progress
        if (booking.getStatus() != BookingStatus.IN_PROGRESS)
            throw new IllegalActionException("The booking cannot be modified");

        // Check if all ticket types are available for this booking cineplex
        List<TicketType> availableTicketTypes =
                showtimeController.getAvailableTicketTypes(booking.getShowtime().getId());
        for (TicketType ticketType : ticketTypesCount.keySet())
            if (!availableTicketTypes.contains(ticketType))
                throw new IllegalActionException("Ticket type is not available");

        // Check if number of ticket types exceed maximum seats per booking
        int totalCount = ticketTypesCount.values().stream().mapToInt(Integer::intValue).sum();
        if (totalCount > BookingConfig.getMaxSeatsPerBooking())
            throw new IllegalActionException("Already reached maximum number of seats for booking");

        booking.setTicketTypesCount(ticketTypesCount);
    }

    /**
     * Assign seats to tickets arbitrarily by the order of the selected ticket type when user selects seats.
     * This is to make sure the ticket contains the ticket type and seat information for validation when
     * movie goers enters the cineplex.
     * @param bookingId The ID of the booking whose tickets are assigned.
     * @param seats The seats to be assigned to the booking's tickets.
     * @throws IllegalActionException if the seats are not available for this showtime,
     * or if the number of seats is not the same as the number of selected ticket types,
     * or if the booking is not in progress,
     * or if the seat is not found in this showtime.
     */
    public void selectSeats(UUID bookingId, List<Seat> seats) throws IllegalActionException {

        Booking booking = findById(bookingId);

        // Check if booking not in progress
        if (booking.getStatus() != BookingStatus.IN_PROGRESS)
            throw new IllegalActionException("The booking cannot be modified");

        // Check if the number of seats the same with the tickets
        int totalTicketsCount = booking.getTotalTicketsCount();
        if (seats.size() != totalTicketsCount)
            throw new IllegalActionException("The number of seats does not match the number of ticket types.");

        // Check if all seats are available
        for (Seat seat : seats)
            if (!booking.getShowtime().getSeating().isAvailable(seat))
                throw new IllegalActionException("Seat is unavailable");

        booking.setSeats(seats);
    }

    /**
     * Cancels the booking with the given booking ID in the process of making a booking.
     * A booking cannot be cancelled once confirmed.
     * @param bookingId The ID of the booking to be cancelled.
     * @exception IllegalActionException if the booking has been confirmed.
     */
    public void cancelBooking(UUID bookingId) throws IllegalActionException {
        Booking booking = findById(bookingId);
        BookingStatus previousStatus = booking.getStatus();
        if (previousStatus == BookingStatus.CONFIRMED)
            throw new IllegalActionException("The booking can not be cancelled");
        booking.setStatus(BookingStatus.CANCELLED);
    }

    /**
     * Assign the booking to the user after booking is confirmed. The status of the booking will be set to confirmed.
     * The user will be added the booking. The seats of this booking will be changed to taken.
     * @param bookingId The ID of the booking to be assigned.
     * @param userId The ID of the user who will be assigned the booking.
     * @throws IllegalActionException if the movie is not open for booking,
     * or if the payment is not accepted,
     * or if the booking is not in progress previously,
     * or if the seat is not found in this booing's showtime seating.
     */
    public void confirmBooking(UUID bookingId, UUID userId)
            throws IllegalActionException {

        UserController userController = UserController.getInstance();

        Booking booking = findById(bookingId);
        Showtime showtime = booking.getShowtime();

        // Check whether showtime is open for booking
        if (showtime.getStatus() != ShowtimeStatus.OPEN_BOOKING)
            throw new IllegalActionException("Can only book when the movie is open for booking");

        // Check if booking not in progress
        BookingStatus previousStatus = booking.getStatus();
        if (previousStatus != BookingStatus.IN_PROGRESS)
            throw new IllegalActionException("The booking can not be confirmed");

        // Check whether payment is made
        Payment payment = booking.getPayment();
        if (payment.getStatus() != PaymentStatus.ACCEPTED)
            throw new IllegalActionException("The payment is not accepted yet");

        booking.setStatus(BookingStatus.CONFIRMED);
        showtime.addBooking(booking);

        User user = userController.findById(userId);
        user.addBooking(findById(bookingId));

        // Mark all seats for the booking as taken
        ShowtimeSeating seating = showtime.getSeating();
        for (Seat seat : booking.getSeats())
            seating.setSeatingStatus(seat, SeatingStatus.TAKEN);
    }
}

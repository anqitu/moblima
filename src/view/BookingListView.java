package view;

import exception.NavigationRejectedException;
import manager.BookingController;
import manager.UserController;
import model.booking.Booking;
import model.commons.User;
import view.ui.*;

import java.util.ArrayList;

public class BookingListView extends ListView {

    private UserController userController;

    private User user;
    private ArrayList<Booking> bookings;

    private BookingController bookingController;

    public BookingListView(Navigation navigation) {
        super(navigation);
        this.userController = UserController.getInstance();
        this.bookingController = BookingController.getInstance();
    }

    @Override
    public void onLoad(AccessLevel accessLevel, Intent intent, String... args) {
        setTitle("Booking History");

        switch (accessLevel) {

            case ADMINISTRATOR:
                bookings = bookingController.getList();
                break;
            case PUBLIC:
                View.displayInformation("Please enter your mobile number.");
                String mobileNumber = Form.getString("Mobile number");
                user = userController.findByMobile(mobileNumber);
                if (user == null) {
                    View.displayError("User with mobile '" + mobileNumber + "' not found!");
                    Form.pressAnyKeyToContinue();
                    throw new NavigationRejectedException();
                }
                bookings = user.getBookings();
                break;
        }

        setContent("Displaying " + bookings.size() + " booking item(s).");
        addBackOption();
        setViewItems(bookings.stream().map(
                booking -> new ViewItem(new BookingView(booking),
                        booking.getId().toString())).toArray(ViewItem[]::new));
    }

    @Override
    public void onEnter() {
        display();
        String userInput = getChoice();
        if (userInput.equals(BACK))
            navigation.goBack();
        else {
            System.out.println("Booking with UUID " + userInput + " selected!");
        }
    }
}

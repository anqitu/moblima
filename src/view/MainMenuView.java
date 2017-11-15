package view;

import config.AdminConfig;
import view.MovieListView.MovieListIntent;
import view.ui.*;

import java.util.Scanner;


/**
 * This moblima.view displays the user interface of main menu for the user.
 *
 * @version 1.0
 * @since 2017-10-30
 */

public class MainMenuView extends MenuView {

    public String version;

    public MainMenuView(Navigation navigation) {
        super(navigation);
    }

    @Override
    public void onLoad(AccessLevel accessLevel, Intent intent, String... args) {
        this.version = args[0];

        setMenuItems(MainMenuOption.values());
        setTitle("MOBLIMA " + version);
        setContent("Welcome to MOBLIMA APP.");
    }

    @Override
    public void onEnter() {
        display();
        switch (MainMenuOption.valueOf(getChoice())) {
            case SEARCH_MOVIES:
                navigation.goTo(new MovieListView(navigation), AccessLevel.PUBLIC, MovieListIntent.SEARCH_MOVIES);
                break;
            case LIST_MOVIES:
                navigation.goTo(new MovieListView(navigation), AccessLevel.PUBLIC, MovieListIntent.VIEW_MOVIES);
                break;
            case VIEW_SHOWTIMES:
                navigation.goTo(new ShowtimeListView(navigation), AccessLevel.PUBLIC,
                        ShowtimeListView.ShowtimeListIntent.VIEW_SHOWTIMES);
                break;
            case VIEW_BOOKING_HISTORY:
                navigation.goTo(new BookingListView(navigation), AccessLevel.PUBLIC);
                break;
            case VIEW_TOP_5_MOVIES:
                navigation.goTo(new MovieListView(navigation), AccessLevel.PUBLIC, MovieListIntent.RANK_MOVIES);
                break;
            case ADMIN:
                navigation.goTo(new AdminMenuView(navigation), AccessLevel.PUBLIC);
                break;
        }
    }

    @Override
    public String getChoice() {
        char min = 'A';
        char max = (char) ('A' + menuItems.size() - 1);
        while (true) {
            System.out.print(PROMPT + " [ " + min + "-" + max + " ]" + PROMPT_DELIMITER);
            Scanner sc = new Scanner(System.in);
            String input = sc.next();
            if (input.length() == 1) {
                char charInput = input.charAt(0);
                if (charInput >= min && charInput <= max)
                    return menuItems.get(charInput - 'A').getValue();
                else
                    View.displayError(UNRECOGNIZED_ERROR);
            } else if (input.equals(AdminConfig.getAdminSecret()))
                return "ADMIN";
            else
                View.displayError(UNRECOGNIZED_ERROR);
        }
    }

    public enum MainMenuOption implements EnumerableMenuOption {

        SEARCH_MOVIES("Search Movies"),
        LIST_MOVIES("List Movies"),
        VIEW_SHOWTIMES("View Showtimes"),
        VIEW_BOOKING_HISTORY("View Booking History"),
        VIEW_TOP_5_MOVIES("View Top 5 Movies"),
        ADMIN(null);

        private String description;

        MainMenuOption(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }
}

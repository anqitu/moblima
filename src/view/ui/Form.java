package view.ui;

import util.Utilities;

import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Represents a base interface that must be implemented by all classes that are user interfaces.
 * It has static methods that are useful for getting inputs.
 * @version 1.0
 * @since 2017-10-20
 */
public interface Form {

    /**
     * The delimiter for prompt.
     */
    String PROMPT_DELIMITER = " > ";

    /**
     * The format of date.
     */
    String DATE_FORMAT = "dd/MM/yyyy HH:mm";

    /**
     * The message to be displayed with the user input is invalid.
     */
    String INVALID_ERROR = "Invalid user input! Please try again.";

    /**
     * The message to be displayed with the user input is unrecognized.
     */
    String UNRECOGNIZED_ERROR = "Unrecognized user input! Please try again.";

    /**
     * The message to be displayed for confirmation.
     */
    String CONFIRM = "CONFIRM";

    /**
     * The message to be displayed for cancellation.
     */
    String CANCEL = "CANCEL";

    /**
     * Email should be in the valid format.
     */
    Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    /**
     * Credit card should be in the correct format with 13 or 16 digits.

     */
    Pattern CREDIT_CARD_REGEX = Pattern.compile("\\d{13,16}");

    /**
     * Mobile phone should be in the valid format of 8 digits starting with 8 or 9
     */
    Pattern MOBILE_REGEX = Pattern.compile("[89]\\d{7}");

    /**
     * Security code should be 3 or 4 digits
     */
    Pattern CVV_REGEX = Pattern.compile("\\d{3,4}");

    /**
     * Gets integer input which has a maximum limit. A invalid error message will be displayed
     * if the input integer exceeding the maximum limit.
     * @param prompt The message to prompt the user to enter input.
     * @param max The maximum limit that the integer cannot be.
     * @return the integer input from the user which is not larger than the maximum limit.
     */
    static int getIntWithMax(String prompt, int max) {
        while (true) {
            int input = getInt(prompt + " [ <=" + max + " ]");
            if (input <= max)
                return input;
            else
                View.displayError(INVALID_ERROR);
        }

    }

    /**
     * Gets integer input which has a minimum limit. A invalid error message will be displayed
     * if the input integer is below the minimum limit.
     * @param prompt The message to prompt the user to enter input.
     * @param min The minimum limit that the integer can be.
     * @return the integer input from the user which is smaller than the minimum limit.
     */
    static int getIntWithMin(String prompt, int min) {
        while (true) {
            int input = getInt(prompt + " [ >=" + min + " ]");
            if (input >= min)
                return input;
            else
                View.displayError(INVALID_ERROR);
        }

    }

    /**
     * Gets integer input from the user. An unrecognized message error will be displayed if the input does not match.
     * @param prompt The message to prompt the user to enter input.
     * @return the integer input from the user.
     */
    static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt + PROMPT_DELIMITER);
            Scanner sc = new Scanner(System.in);
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                View.displayError(UNRECOGNIZED_ERROR);
            }
        }
    }

    /**
     * Gets integer input from the user. An invalid error message will be displayed
     * if the input integer is not within indicated range.
     * @param prompt The message to prompt the user to enter input.
     * @param min The minimum limit that the integer can be.
     * @param max The maximum limit that the integer can be.
     * @return the integer input which is within the given range.
     */
    static int getInt(String prompt, int min, int max) {
        while (true) {
            int input = getInt(prompt + " [ " + min + "-" + max + " ]");
            if (input >= min && input <= max)
                return input;
            else
                View.displayError(INVALID_ERROR);
        }

    }

    /**
     * Gets double input from the user. An unrecognized message error will be displayed if the input does not match.
     * @param prompt The message to prompt the user to enter input.
     * @return the double input entered by the user.
     */
    static double getDouble(String prompt) {

        while (true) {
            System.out.print(prompt + PROMPT_DELIMITER);
            Scanner sc = new Scanner(System.in);
            try {
                return sc.nextDouble();
            } catch (InputMismatchException e) {
                View.displayError(UNRECOGNIZED_ERROR);
            }
        }
    }

    /**
     * Gets double input from the user. An invalid error message will be displayed
     * if the input double is not within indicated range.
     * @param prompt The message to prompt the user to enter input.
     * @param min The minimum limit that the double can be.
     * @param max The maximum limit that the double can be.
     * @return the double input which is within the given range.
     */
    static double getDouble(String prompt, double min, double max) {
        while (true) {
            double input = getDouble(prompt + " [ " + min + "-" + max + " ]");
            if (input >= min && input <= max)
                return input;
            else
                View.displayError(INVALID_ERROR);
        }

    }

    /**
     * Gets double input which has a minimum limit. A invalid error message will be displayed
     * if the input double is below the minimum limit.
     * @param prompt The message to prompt the user to enter input.
     * @param min The minimum limit that the double can be.
     * @return the double input from the user which is smaller than the minimum limit.
     */
    static double getDoubleWithMin(String prompt, double min) {
        while (true) {
            double input = getDouble(prompt + " [ <=" + min + " ]");
            if (input >= min)
                return input;
            else
                View.displayError(INVALID_ERROR);
        }
    }

    /**
     * Gets the censored string from the user which is invisible on the screen.
     * @param prompt The message to prompt the user to enter input.
     * @return the censored string entered by the user.
     */
    static String getCensoredString(String prompt) {
        Console console = System.console();
        return new String(console.readPassword(prompt + PROMPT_DELIMITER));
    }

    /**
     * Gets the string from the user which must be only one word.
     * @param prompt The message to prompt the user to enter input.
     * @return the input string from the user.
     */
    static String getString(String prompt) {
        return getString(prompt, 1);
    }

    /**
     * Gets the string from the user whose number of words must not be smaller than the given limit.
     * If the number of words entered is smaller than the minimum limit, an invalid error message will be displayed.
     * @param prompt The message to prompt the user to enter input.
     * @param minWords The minimum number of words that the string must have.
     * @return the input string from the user.
     */
    static String getString(String prompt, int minWords) {

        while (true) {
            System.out.print(prompt + PROMPT_DELIMITER);
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            if (minWords == 0)
                return input;
            else if (!input.trim().equals("") && input.split(" ").length >= minWords)
                return input;
            else
                View.displayError(INVALID_ERROR);
        }
    }

    /**
     * Gets the input that must match with the given pattern.
     * @param prompt The prompt to get the input.
     * @param regexPattern The pattern that the input must match with.
     * @return the input that matches with the given pattern.
     */
    static String getString(String prompt, Pattern regexPattern) {
        while (true) {
            String input = getString(prompt);
            if (regexPattern.matcher(input).matches())
                return input;
            View.displayError(INVALID_ERROR);
        }
    }

    /**
     * Gets an option from the user from the given array of menu options displayed.
     * @param prompt The message to prompt the user to enter input.
     * @param genericMenuOptions The manually created menu options.
     * @return the input string from the user.
     */
    static String getOption(String prompt, GenericMenuOption... genericMenuOptions) {

        char itemIndex = 'A';
        MenuItem[] options = new MenuItem[genericMenuOptions.length];
        for (int i = 0; i < genericMenuOptions.length; i++, itemIndex++) {
            String description = genericMenuOptions[i].getDescription();
            if (description != null) {
                options[i] = new MenuItem(description, genericMenuOptions[i].getValue());
                options[i].display(itemIndex);
            }
        }

        int index = Form.getChar(prompt, 'A', (char) ('A' + options.length - 1)) - 'A';
        return options[index].getValue();
    }

    /**
     * Gets an option from the user from the given array of menu options displayed.
     * @param prompt The message to prompt the user to enter input.
     * @param enumerableMenuOptions The standard set of menu options.
     * @return the input string from the user.
     */
    static String getOption(String prompt, EnumerableMenuOption... enumerableMenuOptions) {
        return getOption(prompt, Arrays.stream(enumerableMenuOptions).map(menuOption ->
                new GenericMenuOption(menuOption.getDescription(),
                        menuOption.name())).toArray(GenericMenuOption[]::new));
    }

    /**
     * Gets the confirmation or cancellation option.
     * @param confirmText The text of confirmation.
     * @param cancelText The text of cancellation.
     * @return The option from the user.
     */
    static String getConfirmOption(String confirmText, String cancelText) {
        return getOption("Confirmation", new GenericMenuOption(confirmText, CONFIRM),
                new GenericMenuOption(cancelText, CANCEL));
    }

    /**
     * Gets a character from the user.
     * An unrecognised error message will be displayed if the input is not a single character.
     * @param prompt The message to prompt the user to enter input.
     * @return the input string from the user.
     */
    static char getChar(String prompt) {
        while (true) {
            System.out.print(prompt + PROMPT_DELIMITER);
            Scanner sc = new Scanner(System.in);
            String input = sc.next();
            if (input.length() == 1)
                return input.charAt(0);
            else
                View.displayError(UNRECOGNIZED_ERROR);
        }


    }

    /**
     * Gets a character from the user.
     * An unrecognised error message will be displayed if the input is not within the given range.
     * @param prompt The message to prompt the user to enter input.
     * @param min The character which the character can not be before it alphabetically.
     * @param max The character which the character can not be after it alphabetically.
     * @return the input character from the user.
     */
    static char getChar(String prompt, char min, char max) {
        while (true) {
            char input = getChar(prompt + " [ " + min + "-" + max + " ]");
            if (input >= min && input <= max)
                return input;
            else
                View.displayError(INVALID_ERROR);
        }
    }

    /**
     * Gets the date input from the user. An unrecognized error message will be displayed
     * if the input is not in a date format.
     * @param prompt The message to prompt the user to enter input.
     * @param format The given format that the input is expected to be.
     * @return the formatted version of the date input from the user
     */
    static Date getDate(String prompt, String format) {
        while (true) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            System.out.print(prompt + " [ " + format + " ]" + PROMPT_DELIMITER);
            Scanner sc = new Scanner(System.in);
            try {
                return dateFormat.parse(sc.nextLine());
            } catch (ParseException e) {
                View.displayError(UNRECOGNIZED_ERROR);
            }
        }

    }

    static Date getTime(String prompt, Date date, boolean afterNow) {

        if (afterNow && Utilities.getStartOfDate(date).before(Utilities.getStartOfDate(new Date())))
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);

        Date time;
        do {
            time = Form.getDate(prompt, "HH:mm");
            calendar.setTime(time);
            calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
            calendar.set(Calendar.YEAR, year);
            time = calendar.getTime();

            if (afterNow && time.before(new Date()))
                View.displayError("Invalid user input! Time input must be after current time.");
            else
                break;
        } while (true);
        return time;
    }

    /**
     * Gets a boolean input.
     * @param prompt The prompt to ask for the input.
     * @return The boolean input.
     */
    static boolean getBoolean(String prompt) {
        while (true) {
            char response = getChar(prompt + " [ Y/N ]");
            if (response == 'Y' || response == 'N')
                return response == 'Y';
            else
                View.displayError(INVALID_ERROR);
        }
    }

    /**
     * Prompts the user to press any key to continue.
     */
    static void pressAnyKeyToContinue() {
        System.out.print("Press ENTER key to continue...");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }
}

package view.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a base class of moblima.view.
 *
 * @version 1.0
 * @since 2017-10-20
 */
public abstract class View {

    /**
     * The left margin is set to be 1.
     */
    public int LEFT_MARGIN = 1;

    /**
     * Sets the width of the moblima.view to be 100.
     */
    public final static int VIEW_WIDTH = 100;

    /**
     * Sets the date display format of the moblima.view to be "EEEEE, dd, MMMMMM YYYY".
     */
    public static final String DATE_DISPLAY_FORMAT = "EEEEE, dd MMMMM YYYY";

    /**
     * Sets dash line with the length of the moblima.view width.
     */
    public final static String DASH_LINE = line('-', VIEW_WIDTH);

    /**
     * Sets space line with the length of the moblima.view width.
     */
    public final static String SPACE_LINE = line(' ', VIEW_WIDTH);

    /**
     * Sets the colour of the information to be cyan.
     */
    public final static ConsoleColor INFO = ConsoleColor.CYAN;

    /**
     * Sets the colour of the warning to be yellow.
     */
    public final static ConsoleColor WARN = ConsoleColor.YELLOW;

    /**
     * Sets the colour of the error to be red.
     */
    public final static ConsoleColor ERROR = ConsoleColor.RED;

    /**
     * Sets the colour of the success to be green.
     */
    public final static ConsoleColor SUCCESS = ConsoleColor.GREEN;

    /**
     * The title of this moblima.view.
     */
    protected String title;

    /**
     * The array list of content of this moblima.view.
     */
    protected ArrayList<String> content = new ArrayList<>();

    /**
     * Display a message in the given colour.
     * @param message The message to be displayed.
     * @param color The displayed colour of this message.
     */
    public static void displayColored(String message, ConsoleColor color) {
        System.out.print(color.code);
        for (String stringLine : wrap(message, VIEW_WIDTH))
            System.out.println(stringLine);
        System.out.print(ConsoleColor.RESET.code);

    }

    /**
     * Display a information message.
     * @param message the information message to be displayed.
     */
    public static void displayInformation(String message) {
        displayColored(message, INFO);
        System.out.println();
    }

    /**
     * Display a warning message.
     * @param message the warning message to be displayed.
     */
    public static void displayWarning(String message) {
        displayColored(message, WARN);
        System.out.println();
    }

    /**
     * Display a error message.
     * @param message the error message to be displayed.
     */
    public static void displayError(String message) {
        displayColored(message, ERROR);
        System.out.println();
    }

    /**
     * Display a success message.
     * @param message the success message to be displayed.
     */
    public static void displaySuccess(String message) {
        displayColored(message, SUCCESS);
        System.out.println();
    }

    /**
     * Gets a line of the given character of the given length.
     * @param character The character to be printed.
     * @param length The length of this line of characters.
     * @return a line of the given character of this given length.
     */
    protected static String line(char character, int length) {
        String line = String.format("%0" + length + "d", 0);
        return line.replace("0", String.valueOf(character));
    }

    /**
     * Gets a strings of of given text wrapped according to the given length.
     * @param text The text to be wrapped.
     * @param length The length of the moblima.view in which the text is displayed.
     * @return a strings of of this given text wrapped according to this given length.
     */
    protected static String[] wrap(String text, int length) {
        ArrayList<String> lines = new ArrayList<>();
        Pattern regex = Pattern.compile(String.format("(.{1,%s}(?:\\s|$))|(.{0,10})", length),
                Pattern.DOTALL);
        Matcher regexMatcher = regex.matcher(text);
        while (regexMatcher.find()) {
            String match = regexMatcher.group();
            if (match.length() > 0)
                lines.add(match);
        }
        return lines.toArray(new String[lines.size()]);
    }

    /**
     * Gets the title of this moblima.view.
     * @return the title of this moblima.view.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Changes the title of this moblima.view.
     * @param title The new title of this moblima.view.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the content of this moblima.view.
     * @return the content of this moblima.view in the form of a list of strings.
     */
    public String[] getContent() {
        return content.toArray(new String[content.size()]);
    }

    /**
     * Sets the content of this moblima.view.
     * @param content the content of this moblima.view in the form of a list of strings to be set.
     */
    public void setContent(String... content) {
        this.content.clear();
        this.content.addAll(Arrays.asList(content));
    }

    /**
     * Sets content fot his moblima.view.
     * @param content The content to be set for this moblima.view.
     */
    public void setContent(List<String> content) {
        this.content.clear();
        this.content.addAll(content);
    }

    /**
     * Display the title of this moblima.view.
     */
    public void displayTitle() {
        System.out.println(DASH_LINE);
        System.out.println(title);
        System.out.println(DASH_LINE);
    }

    /**
     * Display the content of this moblima.view.
     */
    public void displayContent() {
        for (String line : content)
            for (String stringLine : wrap(line, VIEW_WIDTH))
                System.out.println(stringLine);
        System.out.println();
    }

    /**
     * Display the title and content of this moblima.view.
     */
    public void display() {
        displayTitle();
        displayContent();
    }

    /**
     * Display the title and content of this moblima.view in one line.
     * @param titleDelimiter the delimiter of the title of this moblima.view.
     * @param contentDelimiter the delimiter of the content of this moblima.view.
     * @return the string of the title and content of this moblima.view in one line.
     */
    public String flatten(String titleDelimiter, String contentDelimiter) {
        return title + titleDelimiter + String.join(contentDelimiter, content);
    }
}

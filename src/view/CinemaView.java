package view;

import model.booking.ShowtimeSeating;
import model.cineplex.Cell;
import model.cineplex.CinemaLayout;
import model.cineplex.Seat;
import view.ui.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 * This moblima.view displays the user interface for the user moblima.view cineplex seating.
 *
 * @version 1.0
 * @since 2017-10-30
 */
public class CinemaView extends View {

    public CinemaView(ShowtimeSeating showtimeSeating, CinemaLayout cinemaLayout, List<Seat> selectedSeats) {

        Hashtable<Character, Cell[]> cellLayout = cinemaLayout.getLayout();
        String columnAxis = "  ";
        for (int columnLabel = 1; columnLabel <= cinemaLayout.getMaxColumn(); columnLabel++)
            columnAxis += " " + columnLabel + (columnLabel >= 10 ? "" : " ");

        String leftMargin = View.line(' ', LEFT_MARGIN);

        ArrayList<String> content = new ArrayList<>();

        // Screen
        int screenWidth = 3 * cinemaLayout.getMaxColumn() + 4;
        content.add(leftMargin + View.line('-', screenWidth));
        content.add(leftMargin + View.line(' ', screenWidth/2 - 3) + "SCREEN");
        content.add(leftMargin + View.line('-', screenWidth));

        content.add(leftMargin + columnAxis);
        for (char rowLabel = 'A'; rowLabel <= cinemaLayout.getMaxRow(); rowLabel++) {
            String row = rowLabel + " ";
            for (Cell cell : cellLayout.get(rowLabel)) {
                if (cell instanceof Seat) {
                    Seat seat = (Seat) cell;
                    char seatIcon = showtimeSeating.getSeatingStatus(seat).toString().charAt(0);
                    if (selectedSeats != null)
                        for (Seat selectedSeat : selectedSeats)
                            if (seat.getRow() == selectedSeat.getRow() && seat.getColumn() == selectedSeat.getColumn()) {
                                seatIcon = '!';
                                break;
                            }
                    row += cell.toStringIcon().replace(' ', seatIcon);
                } else {
                    row += cell.toStringIcon();
                }
            }
            content.add(leftMargin + row + " " + rowLabel);
        }
        content.add(leftMargin + columnAxis);
        content.addAll(Arrays.asList(
                " ",
                "Legend:",
                "Available                   [ ]",
                "Couple Seat              [    ]",
                "Wheelchair Accessible       { }",
                "Occupied                    [X]"
                ));

        if (selectedSeats != null)
            content.add("Your Seat                   [!]");

        setContent(content.toArray(new String[content.size()]));
    }
}

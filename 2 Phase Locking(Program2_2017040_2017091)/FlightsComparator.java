import java.util.Comparator;

public class FlightsComparator implements Comparator<Flight> {
    @Override
    public int compare(Flight flight, Flight t1) {
        return flight.getFlight_name().compareTo(t1.getFlight_name());
    }
}

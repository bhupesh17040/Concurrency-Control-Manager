import java.util.ArrayList;
import java.util.Collections;

public class Database {

  private  ArrayList<Flight> flights;



  public  Database(int x){
      flights=new ArrayList<Flight>();
      for(int i=0;i<x;++i){
          Flight temp=new Flight("flight"+i);
          flights.add(temp);
      }
      Collections.sort(this.flights,new FlightsComparator());

  }

  public Flight get_flight(String flight){
      for(Flight f:flights){
          if(flight.compareTo(f.getFlight_name())==0)return f;
      }
      return null;
  }


  public void sort_db(){
      Collections.sort(this.flights,new FlightsComparator());
  }

    public ArrayList<Flight> getFlights() {
        return flights;
    }
}

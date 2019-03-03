import java.util.ArrayList;
import java.util.Collections;

public class Concurrency_Control_Manager {

    private Database db;

    public Concurrency_Control_Manager(int total_flights){
        db=new Database(total_flights);
    }


    public synchronized Flight get_lock_on_flight(String flight,int lock_type,int tid){
        Flight temp_flight=  db.get_flight(flight);
        if(temp_flight==null)return null;
        //lock_type==1 for shared lock
        //lock_type==2 for exclusive lock
        if(lock_type==1)temp_flight.get_shared_lock(tid);
        if(lock_type==2)temp_flight.get_exclusive_lock(tid);
        long start=System.currentTimeMillis();
        long stop=System.currentTimeMillis();
        while(stop-start<1){
            stop=System.currentTimeMillis();
        }
        return temp_flight;
    }

    public synchronized void return_lock_on_flight(Flight flight,int lock_type,int tid){
        if(lock_type==1){
            flight.release_shared_lock(tid);
        }
        if(lock_type==2){
            flight.release_exclusive_lock(tid);
        }
    }


    public synchronized ArrayList<Flight> get_lock_on_all(int tid){
        ArrayList<Flight>flights=db.getFlights();
        Collections.sort(flights,new FlightsComparator());
        for(Flight f:flights){
            f.get_shared_lock(tid);
            long start=System.currentTimeMillis();
            long stop=System.currentTimeMillis();
            while(stop-start<1){
                stop=System.currentTimeMillis();
            }
        }
        return flights;
    }
    public synchronized void release_all_lock(ArrayList<Flight>flights,int tid){
        for (Flight f:flights){
            f.release_shared_lock(tid);
        }
    }

}

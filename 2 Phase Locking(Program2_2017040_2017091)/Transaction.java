import java.util.ArrayList;

public class Transaction extends Thread {
   private Integer transaction_type;
   static int temp_tid=0;
   int tid;
   private String flight1;
   private String flight2;
   private Integer id;
   ArrayList<String > lock_acquired=new ArrayList<String>();
   private Concurrency_Control_Manager manager;
    public Transaction(Integer transaction_type,Concurrency_Control_Manager manager,String flight1,String flight2,Integer id){
        this.manager=manager;
        this.flight1=flight1;
        this.flight2=flight2;
        this.id=id;
        this.transaction_type=transaction_type;
        tid=temp_tid;
        temp_tid++;

    }

    public Transaction(Integer transaction_type,Concurrency_Control_Manager manager,String flight1,Integer id){
        this.manager=manager;
        this.flight1=flight1;
        this.id=id;
        this.transaction_type=transaction_type;
        tid=temp_tid;
        temp_tid++;
    }

    public Transaction(Integer transaction_type,Concurrency_Control_Manager manager,Integer id){
        this.manager=manager;
        this.id=id;
        this.transaction_type=transaction_type;
        tid=temp_tid;
        temp_tid++;
    }

    public Transaction(Integer transaction_type,Concurrency_Control_Manager manager){
        this.manager=manager;
        this.transaction_type=transaction_type;
        tid=temp_tid;
        temp_tid++;

    }



    //method to reserve a flight
    public void reserve(String sflight,Integer id)  {
        try{Thread.sleep(20);}
        catch (InterruptedException e){

        }
        Flight flight=manager.get_lock_on_flight(sflight,2,tid);
        if(flight==null)return;
        lock_acquired.add(flight.getFlight_name());
        System.out.println("exclusive lock acquired on "+flight.getFlight_name());;
        flight.add(id);
        System.out.println("added "+id+" in "+sflight);
        System.out.println();
        manager.return_lock_on_flight(flight,2,tid);
    }

    //method to cancle a flight
    public void cancle(String sflight,Integer id)  {
        try{Thread.sleep(20);}
        catch (InterruptedException e){

        }
        Flight flight=manager.get_lock_on_flight(sflight,2,tid);

        if(flight==null)return;
        lock_acquired.add(flight.getFlight_name());
        System.out.println("exclusive lock acquired on "+flight.getFlight_name());;
        flight.remove(id);
        System.out.println("removed "+id+" in "+sflight);
        System.out.println();
        manager.return_lock_on_flight(flight,2,tid);
    }

    //method to return the flight where id is found
    public ArrayList<String> my_flights(Integer  id) {
        try{Thread.sleep(100);}
        catch (InterruptedException e){

        }
        ArrayList<String> record=new ArrayList<String>();
        ArrayList<Flight> flights=manager.get_lock_on_all(tid);

        for(Flight f:flights){
            lock_acquired.add(f.getFlight_name());
        }
        System.out.println("got shared lock on database");
        for(Flight flight:flights){
            if(flight.get_id(id))record.add(flight.getFlight_name());
        }
        System.out.println("record calculated");

        for(String i:record){
            System.out.print(i+" ");
        }
        System.out.println();
        manager.release_all_lock(flights,tid);
        return record;
    }

    //method to return the sum of all the reservation on all the flights
    public Integer total_reservation()  {
        try{Thread.sleep(100);}
        catch (InterruptedException e){

        }
        int reservations=0;
        ArrayList<Flight>flights=manager.get_lock_on_all(tid);
        for(Flight f:flights){
            lock_acquired.add(f.getFlight_name());
        }
        System.out.println("got shared lock on database");
        for(Flight flight:flights){
            reservations+=flight.getTotal();
        }
        System.out.println("total reservation "+reservations);
        System.out.println();
        manager.release_all_lock(flights,tid);
        return reservations;
    }


    //method to transfer a passenger from 1 flight to another flight
    public void tranfer(String s_flight_1,String s_flight_2,Integer id)  {
        Flight temp_flight1,temp_flight2;
        try{Thread.sleep(40);}
        catch (InterruptedException e){

        }
        if(s_flight_1.compareTo(s_flight_2)==0){
            System.out.println("return");
            return;
        }
        if(s_flight_1.compareTo(s_flight_2)<=0){
                temp_flight1=manager.get_lock_on_flight(s_flight_1,2,tid);
                temp_flight2=manager.get_lock_on_flight(s_flight_2,2,tid);
            }
        else {
            temp_flight2=manager.get_lock_on_flight(s_flight_2,2,tid);
            temp_flight1=manager.get_lock_on_flight(s_flight_1,2,tid);
        }
        if(temp_flight1==null || temp_flight1==null)return;
        lock_acquired.add(temp_flight1.getFlight_name());lock_acquired.add(temp_flight2.getFlight_name());
        System.out.println("exclusive lock acquired on "+temp_flight1.getFlight_name()+" "+temp_flight2.getFlight_name());;
        temp_flight1.remove(id);
        temp_flight2.add(id);
        System.out.println("lock released on all");
        System.out.println();
        manager.return_lock_on_flight(temp_flight1,2,tid);
        manager.return_lock_on_flight(temp_flight2,2,tid);



    }


    @Override
    public void run() {
        if(transaction_type==1){
            System.out.println("reserve "+flight1+" "+id);
            reserve(flight1,id);
        }
        else if(transaction_type==2){
            System.out.println("cancle "+flight1+" "+id);
            cancle(flight1,id);
        }
        else if(transaction_type==3){
            System.out.println("myflights "+id);
            my_flights(id);
        }
        else if(transaction_type==4){
            System.out.println("total reservations ");
            total_reservation();
        }
       else  if(transaction_type==5){
            System.out.println("transfer "+flight1+" "+flight2+" "+id);
            tranfer(flight1,flight2,id);
        }
       else{

        }

    }
}

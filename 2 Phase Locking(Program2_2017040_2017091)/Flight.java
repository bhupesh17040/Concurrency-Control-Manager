import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class Flight {
    private String Flight_name;
    private ArrayList<Integer> id;
    private Semaphore lock;


    public  Flight(String name){
        this.Flight_name=name;
        lock=new Semaphore();
        id=new ArrayList<Integer>();
    }

    //get particular id id
    public boolean get_id(int id_no){
        return id.contains(id_no);
    }

    //add id
    public void add(int id_no){
        id.add(id_no);
    }

    public void remove(int id_no){
        int index=id.indexOf(id_no);
        if(index==-1)return;
        id.remove(index);
    }


    public String getFlight_name() {
        return Flight_name;
    }


    //get shared lock on this flight
    public void get_shared_lock(int tid){
        lock.shared_lock_acquire(tid);
        System.out.println(this.Flight_name+" "+lock.getShared()+" sharedlock");
    }

    //release shared lock on this object
    public void release_shared_lock(int tid){
        lock.shared_lock_release(tid);
    }

    //get exclusive lock
    public  void get_exclusive_lock(int tid){
        lock.exclusive_lock_acquire(tid);
        System.out.println(this.Flight_name+" "+lock.getExclusive()+" exclusive lock");
    }

    //release exclusive lock
    public void release_exclusive_lock(int tid){
        lock.exclusive_lock_release(tid);
    }

    public int getTotal() {
        return id.size();
    }
}

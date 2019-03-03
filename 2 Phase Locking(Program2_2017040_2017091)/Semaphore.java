import java.util.ArrayList;

public class Semaphore {
    private volatile ArrayList<Integer> shared ;
    private volatile ArrayList<Integer> exclusive;
    private volatile ArrayList<Integer> waiting_shared;
    private volatile ArrayList<Integer> waiting_exclusive;
    public  Semaphore(){
        shared=new ArrayList<Integer>();
        exclusive=new ArrayList<Integer>();
        waiting_shared=new ArrayList<Integer>();
        waiting_exclusive=new ArrayList<Integer>();
    }


    //acquire shared lock
    public synchronized void  shared_lock_acquire(int tid){
        assert exclusive.size()==0;
        add_to_list(tid,waiting_shared);
        while(this.exclusive.size()>0){
        }
        remove_from_list(tid,waiting_shared);
        add_to_list(tid,shared);

    }

    //release shared lock
    public synchronized void shared_lock_release(int tid){
        remove_from_list(tid,shared);
    }

    //acquire exclusive lock

    public  synchronized void exclusive_lock_acquire(int tid){
        assert shared.size()==0 && exclusive.size()==0;
        add_to_list(tid,waiting_exclusive);
        while(this.shared.size()>0 || this.exclusive.size()>0){

        }
        remove_from_list(tid,waiting_exclusive);
       add_to_list(tid,exclusive);
    }

    //release exclusive lock
    public synchronized void exclusive_lock_release(int tid){
        remove_from_list(tid,exclusive);
    }


    public ArrayList<Integer> getExclusive() {
        return exclusive;
    }

    public ArrayList<Integer> getShared() {
        return shared;
    }

    public void add_to_list(int tid, ArrayList<Integer>a){
        a.add(tid);
    }
    public void remove_from_list(int tid,ArrayList<Integer>a){
        a.remove(a.indexOf(tid));
    }
}

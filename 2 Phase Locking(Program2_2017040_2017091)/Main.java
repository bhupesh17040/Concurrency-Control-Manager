import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {



    public static  long[] main(int flights,int transactions) throws InterruptedException {
        int total_flights,total_transactions,ids;
        ids=1;
        HashMap<Integer, String> id_flight_pair=new HashMap<Integer, String>();
        Scanner scan=new Scanner(System.in);
        System.out.print("Enter total flights ");
        System.out.println();
        total_flights=flights;
        System.out.print("Enter total transactions ");
        System.out.println();
        total_transactions=transactions;


        Concurrency_Control_Manager manager=new Concurrency_Control_Manager(total_flights);
        ExecutorService	exec= Executors.newCachedThreadPool();
        ArrayList<Transaction> all_transactions=new ArrayList<Transaction>();
        ArrayList<Transaction> reserve_transactions=new ArrayList<Transaction>();

        for(int i=0;i<10;++i){
            String x="flight"+(int)(Math.random()*total_flights);
            Transaction temp=new Transaction(1,manager,x,ids);
            reserve_transactions.add(temp);
            id_flight_pair.put(ids,x);
            ids++;
        }

        for(int i=0;i<total_transactions;++i){
            int random=(int)(Math.random()*4+2);
            if(random==2){
                String x="";
                int id=-1;
                for(int j:id_flight_pair.keySet()){
                    id=j;
                    x=id_flight_pair.get(j);
                    break;
                }
                if(id==-1)break;
                Transaction temp=new Transaction(2,manager,x,id);
                all_transactions.add(temp);
                id_flight_pair.remove(id);

            }
            else if(random==3 ){
                int id=-1;
                for(int j:id_flight_pair.keySet()){
                    id=j;
                    break;
                }
                if(id==-1)break;
                Transaction temp=new Transaction(3,manager,id);
                all_transactions.add(temp);


            }
            else if(random==4 ){
                    Transaction temp=new Transaction(4,manager);
                    all_transactions.add(temp);
            }
            else if(random==5){
                String x1="flight"+(int)(Math.random()*total_flights);
                String x="";
                int id=-1;
                for(int j:id_flight_pair.keySet()){
                    id=j;
                    x=id_flight_pair.get(j);
                    break;
                }
                if(id==-1)break;;
                Transaction temp=new Transaction(5,manager,x,x1,id);
                all_transactions.add(temp);
                id_flight_pair.remove(id);
                id_flight_pair.put(id,x1);

            }
            else {

            }

        }
        long start=System.currentTimeMillis();
        for(Transaction t:reserve_transactions){
           //exec.execute(t);
            //t. manager.return_lock_on_flight(temp_flight1,2);run();
            t.run();
           // t.join();

        }

        for(Transaction t:reserve_transactions){
            t.join();
        }
        for(Transaction t:all_transactions){
            t.join();
        }
        for(Transaction t:all_transactions){
            //exec.execute(t);
            //t. manager.return_lock_on_flight(temp_flight1,2);run();
            t.run();
            // t.join();

        }
//        boolean finished=false;
//        while(!finished){
//            finished = exec.awaitTermination(1, TimeUnit.MILLISECONDS);
//        }

        long stop=System.currentTimeMillis();
        System.out.println("Time taken "+(stop-start));
        System.out.println("Throughput "+((total_transactions*2)*1000)/((stop-start)));
        long [] time_throughput=new long[2];
        time_throughput[0]=stop-start;
        time_throughput[1]=((total_transactions*2)*1000)/(stop-start);
        return  time_throughput;
 //       int hi=0;
//
//        exec.shutdown();
//        try {
//
//            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            System.out.println("Timelimit over");
//        }

    }

}

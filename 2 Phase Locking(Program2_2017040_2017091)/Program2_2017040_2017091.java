import java.util.ArrayList;

public class Program2_2017040_2017091 {

    public static void main(String[]args) throws InterruptedException {
        ArrayList<Long> a=new ArrayList<Long>();
        for(int i=10;i<200;i+=10){
            long[] time_taken_throughput=new long[2];
            long time_taken,throughput;
            time_taken=throughput=0;
            for(int j=0;j<10;++j){
                time_taken_throughput=Main.main(2,i);
                time_taken+=time_taken_throughput[0];
                throughput+=time_taken_throughput[1];
            }
            a.add(time_taken/10);
            a.add(throughput/10);

        }
        for(int i=0;i<a.size()-1;i+=2){
            System.out.println("time taken average "+a.get(i));
            System.out.println("throughput average "+a.get(i+1));
        }
    }
}

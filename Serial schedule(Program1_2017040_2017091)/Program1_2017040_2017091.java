// DBMS HOMEWORK ASSIGNMENT-2
import java.util.*;
import java.util.concurrent.locks.*;
 class ownLock      // Creating own lock class
 {
	private boolean isLock=false;
	public synchronized void lock() throws InterruptedException
  	{
     while(isLock)
     {
      wait();
     }
     isLock=true;
  }
   public synchronized void unlock()
   {
    isLock=false;
    notify();
  }
}
class Flight
{
	int f;
	int seats;   // Total number of seats
	int reserved; // Total number of reserved seats
	HashMap<Integer,Passenger> p_map=new HashMap<Integer,Passenger>();
	public Flight(int fl,int s)
	{
		f=fl;
		seats=s;
		reserved=0;
	}
}
class Passenger
{
	HashMap<Integer,Flight> f_map=new HashMap<Integer,Flight>();
	int passenger_id;
	public Passenger(int id)
	{
		passenger_id=id;
	}
	public void set_Flight(Flight fl)  // Sets the flight for a particular passenger
	{
		f_map.put(fl.f,fl);
	}
	public void get_Flight(Flight fl)  // Retrieves the flight for a particular passenger
	{
		f_map.get(fl.f);
	}
}
class datab   // Database
{
	volatile ArrayList<Passenger> passengern=new ArrayList<Passenger>();
	volatile ArrayList<Flight> flightn=new ArrayList<Flight>();
	volatile ArrayList<Transaction> transaction=new ArrayList<Transaction>();
	volatile HashMap<Integer,Passenger> passenger_map=new HashMap<Integer,Passenger>();
	volatile HashMap<Integer,Flight> flight_map=new HashMap<Integer,Flight>();
}
class Transaction implements Runnable
{
	datab db;
	static ownLock l=new ownLock();
	int total_flights;
	int total_passengers;
	void Cancel(int f1,int p_id)
	{
		try
		{
			Thread.sleep(200);   // Thread goes to sleep.This has been done in accordance to problem statement.
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		} 
		Passenger p1=db.passenger_map.get(p_id);
		Flight fl1=db.flight_map.get(f1);
		if(fl1.p_map.get(p_id)==null)
		{
			System.out.println("No booking for this flight by the passenger " + p_id + " ");
		}
		else
		{
			p1.f_map.remove(f1);
			fl1.p_map.remove(p_id);
			System.out.println("Flight cancelled");
			fl1.reserved=fl1.reserved-1;
		}
	}
	void Reserve(int f1,int p_id)
	{
		try
		{
			Thread.sleep(200);   // Thread goes to sleep.This has been done in accordance to problem description
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		if(db.flight_map.get(f1).p_map.get(p_id)!=null)
		{
			System.out.println("The flight is already booked by passenger " + p_id + " ");
		}
		else
		{
			Passenger p1=db.passenger_map.get(p_id);
			Flight fl1=db.flight_map.get(f1);
			fl1.reserved=fl1.reserved+1;    // Increasing the number of reserved seats by one
			if(fl1.reserved>fl1.seats)      // No more seats can be reserved
			{
				System.out.println("All seats are reserved for the flight");
			}
			else
			{	
				System.out.println("Reservation successful for passenger " + p_id + " ");
				p1.f_map.put(f1,fl1);
				fl1.p_map.put(p_id,p1);
			} 
		} 
	}
	void Transfer(int F1,int F2,int p_id)
	{
		try
		{
			Thread.sleep(200);   // Thread goes to sleep.This has been done in accordance to problem description
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		Passenger p1=db.passenger_map.get(p_id);
		Flight fl1=db.flight_map.get(F1);
		if(db.passenger_map.get(p_id)==null)
		{
			System.out.println("Transfer failed(Passenger did not book flight F1)");
		}
		else
		{ 
			// Transfer() operation first involves cancelling one flight(F1) and assigning it to next flight(F2)
			p1.f_map.remove(p_id);
			fl1.p_map.remove(F1);
			if(db.flight_map.get(F2).p_map.get(p_id)!=null)
			{
				System.out.println("Transfer not possible for this passenger " + p_id + " ");
			}
			else
			{
				Passenger p2=db.passenger_map.get(p_id);
				Flight f2=db.flight_map.get(F2);
				p2.f_map.put(F2,f2);
				f2.p_map.put(p_id,p2);
				f2.reserved=f2.reserved+1;
				System.out.println("Transfer successful for passenger " + p_id + " ");
			}
			
		}

	}
	int Total_Reservations()
	{	
		try
		{
			Thread.sleep(200);   // Thread goes to sleep.This has been done in accordance to problem description
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		int number=0;
		int r=db.flightn.size();
		for(int i=0;i<r;i++)
		{
			number=number + db.flightn.get(i).reserved;
		}
		return number;
	}
	ArrayList<Flight> My_Flights(int p_id)
	{
		try 
		{
			Thread.sleep(200);  // Thread goes to sleep.This has been done in accordance to problem description
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		ArrayList<Flight> flightlist=new ArrayList<>();
		int s=db.flightn.size();
		for(int j=0;j<s;j++)
		{
			Flight fl1=db.flightn.get(j);
			if(fl1.p_map.get(p_id)!=null)
			{
				flightlist.add(fl1);
			}
		}
		return flightlist;
	}
	@Override
	public void run()   // Overriding the run() function.
	{
		 try 
		 {
		  Thread.sleep(900);
		 } 
		 catch (InterruptedException e)
		 {
		  e.printStackTrace();
		}
		
		int count=1;
		while(count<=5)
		{
			try 
			{
			 Thread.sleep(900);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			// Simulation begins
			int transaction_type;
			Random rand=new Random();
			transaction_type=rand.nextInt(5)+1;
			if(transaction_type==1)
			{
				System.out.println("CANCEL OPERATION EXECUTED");
				int f1=rand.nextInt(total_flights)+1;
				int p_id=rand.nextInt(total_passengers)+1;
				Cancel(f1,p_id);
			}
			else if(transaction_type==2)
			{
				System.out.println("RESERVE OPERATION EXECUTED");
				int f1=rand.nextInt(total_flights)+1;
				int p_id=rand.nextInt(total_passengers)+1;
				Reserve(f1,p_id);
			}
			else if(transaction_type==3)
			{
				System.out.println("My_Flights");
				int p_id=rand.nextInt(total_passengers)+1;
				ArrayList<Flight> fl2=My_Flights(p_id);
				int quant=fl2.size();
				if(quant!=0)
				{
					for(int i=0;i<quant;i++)
					{
						System.out.print("RESERVED FLIGHT ID FOR PASSENGER " + p_id + ":");
						System.out.print(fl2.get(i).f + " ");
					}
				}
				else
				{
					System.out.print("NO RESERVED FLIGHT ID FOR PASSENGER" + p_id + " ");
				}

			}
			else if(transaction_type==4)
			{
				System.out.println("TRANSFER EXECUTED");
				int F1=rand.nextInt(total_flights)+1;
				int F2=rand.nextInt(total_flights)+1;
				int p_id=rand.nextInt(total_passengers)+1;
				Transfer(F1,F2,p_id);

			}
			else
			{
				System.out.println("TOTAL RESERVATIONS" + "\n" + Total_Reservations());
			}
			count=count+1;
			}
			l.unlock();
		}
}
public class Program1_2017040_2017091
{
	public static void main(String args[])
	{
		Random rand1=new Random();
		datab object1=new datab();  // Database object
		int total_passengers=rand1.nextInt(4) + 5;
		int total_flights=rand1.nextInt(6) + 5;
		System.out.println("Total passengers " + total_passengers);
		System.out.println("Total flights " + total_flights);
		for(int i=0;i<total_flights;i++)
		{
			Random rand2=new Random();
			int s=rand2.nextInt(6)+5;
			Flight fl3=new Flight(i+1,s);
			object1.flightn.add(fl3);
			object1.flight_map.put(fl3.f,fl3);
		}
		for(int k=0;k<total_passengers;k++)
		{
			int set1=k%total_flights;
			Passenger p2=new Passenger(k+1);
			object1.passenger_map.put(p2.passenger_id,p2);
			object1.passengern.add(p2);
			Flight fl4=object1.flightn.get(set1);
			p2.f_map.put(fl4.f,fl4);
			fl4.p_map.put(p2.passenger_id,p2);
			fl4.reserved=fl4.reserved+1;
		}
		int runs=32;
		Transaction tarray[]=new Transaction[runs];  // Array of total transactions
		for(int i=0;i<runs;i++)
		{
			tarray[i]=new Transaction();
			tarray[i].total_flights=total_flights;
			tarray[i].total_passengers=total_passengers;
			tarray[i].db=object1;
			
		}
		Thread pool[]=new Thread[runs];
		for(int i=0;i<runs;i++)
		{
			pool[i]=new Thread(tarray[i]);				  
		}
		long start= System.currentTimeMillis(); // Time in milliseconds
		for(int y=0; y<runs; y++)
		{
			System.out.print("Thread:" + pool[y] + "is running ");  // Checking which thread is running.
			pool[y].run();  // Running the threads
		}
		long end=System.currentTimeMillis();
		double totalT=(end-start)/1000;
		System.out.println("Total time:" + " " + totalT);
		double throughput=(runs*5)*1000/totalT; //It is shown in seconds to show that the throughput remains near constant.
		System.out.println("Throughput:"+ " " + throughput);
	}
}



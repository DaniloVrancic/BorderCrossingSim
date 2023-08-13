package terminals;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import vehicles.Automobile;
import vehicles.Bus;
import vehicles.Vehicle;

public class PoliceTerminalForOthers extends PoliceTerminal{

	private static int TIME_FOR_BUS_PASSENGER_PROCESS = 100;
	private static int TIME_FOR_AUTOMOBILE_PASSENGER_PROCESS = 500;
	
	public PoliceTerminalForOthers(BlockingQueue<Vehicle<?>> vehicleQueue)
	{
		super(vehicleQueue);
	}
	
	public void processVehicle() {
		if(this.vehicleAtTerminal instanceof Bus)
		{
			super.processVehicle(TIME_FOR_BUS_PASSENGER_PROCESS);			
		}
		else if(this.vehicleAtTerminal instanceof Automobile)
		{
			super.processVehicle(TIME_FOR_AUTOMOBILE_PASSENGER_PROCESS);
		}
	}
	
	@Override
	public Vehicle<?> takeNextVehicle()
	{
		try
		{
				Vehicle<?> temp = vehicleQueue.peek(); //Check out if the next vehicle in the queue is a Bus or an Automobile, if it is, take it from the Queue
				
				if(temp instanceof Automobile || temp instanceof Bus)
				{
					return super.takeNextVehicle();
				}
			
		}
		catch (Exception ex) {
			errorLogger.severe("<Error polling the Vehicle Queue>: " + ex.getMessage());
		}

		return null; //If it's neither a Bus or an Automobile, return null
	}
	
	@Override
	public void work(Vehicle<?> nextVehicle)
	{
		while(vehicleQueue.size() > 0) //Request to process new vehicles until the queue is empty
		{
			if(this.status.equals(TerminalStatus.READY_FOR_PROCESSING))
			{
				this.vehicleAtTerminal = nextVehicle;
				if(this.vehicleAtTerminal == null)
				{
					continue; //later type wait() or notify();
				}
				System.out.println("TOOK VEHICLE: " + this.vehicleAtTerminal.getClass().getName() + " ID: " + this.vehicleAtTerminal.getVehicleId()); //DELETE LATER, SERVES FOR TESTING
				processVehicle();
				//sendToCustoms(CustomsTerminal ct); //implement it later (have this.vehicleAtTerminal.wait() in the method)
				this.vehicleAtTerminal = null;
				this.status = TerminalStatus.AVAILABLE;
			}
			else
			{
				try {
					this.vehicleAtTerminal.wait(); //will get notified once the Customs terminal finishes processing of their vehicle and frees up
				} catch (InterruptedException e) {
					errorLogger.severe("<INTERRUPTED WAIT EXCEPTION>:" + e.getMessage());
				}
			} //end of else
		} //end of while loop
	} //end of work() (Method)
}

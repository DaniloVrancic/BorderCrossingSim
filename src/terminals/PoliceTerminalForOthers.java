package terminals;

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
	
}

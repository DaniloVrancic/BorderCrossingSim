package terminals;

import java.util.concurrent.BlockingQueue;

import vehicles.Truck;
import vehicles.Vehicle;

public class PoliceTerminalForTrucks extends PoliceTerminal {

    private static final int TIME_FOR_TRUCK_PASSENGER_PROCESS = 500;

	public PoliceTerminalForTrucks(BlockingQueue<Vehicle<?>> vehicleQueue)
	{
		super(vehicleQueue);
	}
    
	
    public void processVehicle() {
            super.processVehicle(TIME_FOR_TRUCK_PASSENGER_PROCESS);
    }
    
}

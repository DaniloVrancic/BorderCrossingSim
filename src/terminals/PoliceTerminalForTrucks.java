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

    @Override
    public Vehicle<?> takeNextVehicle() {
        try {
            
                Vehicle<?> temp = vehicleQueue.peek();
                
                if (temp instanceof Truck) { //Only take trucks from the Queue into this Terminal
                    return super.takeNextVehicle();
                }
            
        } catch (Exception ex) {
            errorLogger.severe("<Error polling the Vehicle Queue>: " + ex.getMessage());
        }

        return null;
    } //end of takeNextVehicle (Method)
    
    
}

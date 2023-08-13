package terminals;

import java.util.Queue;
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
        	System.out.println("PROCESSING VEHICLE: " +this.vehicleAtTerminal.getClass().descriptorString() + " id = " + this.vehicleAtTerminal.getVehicleId() + " AT TERMINAL_ID = " + this.id);
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
    
    @Override
	public void work(Vehicle<?> nextVehicle)
	{
		while(vehicleQueue.size() > 0) //Request to process new vehicles until the queue is empty
		{
			if(this.status.equals(TerminalStatus.READY_FOR_PROCESSING))
			{
				this.vehicleAtTerminal = nextVehicle;
				processVehicle();
				//sendToCustoms(CustomsTerminal ct); //implement it later (have this.vehicleAtTerminal.wait() in the method)
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

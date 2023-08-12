package terminals;

import java.util.Queue;

import vehicles.Truck;
import vehicles.Vehicle;

public class PoliceTerminalForTrucks extends PoliceTerminal {

    private static final int TIME_FOR_TRUCK_PASSENGER_PROCESS = 500;

    
    public void processVehicle() {
        
            super.processVehicle(TIME_FOR_TRUCK_PASSENGER_PROCESS);
        
    }

    @Override
    public Vehicle<?> takeNextVehicle(Queue<Vehicle<?>> vehicleQueue) {
        try {
            synchronized (vehicleQueue) {
                Vehicle<?> temp = vehicleQueue.peek();
                
                if (temp instanceof Truck) { //Only take trucks from the Queue into this Terminal
                    return super.takeNextVehicle(vehicleQueue);
                }
            }
        } catch (Exception ex) {
            errorLogger.severe("<Error polling the Vehicle Queue>: " + ex.getMessage());
        }

        return null;
    }
}

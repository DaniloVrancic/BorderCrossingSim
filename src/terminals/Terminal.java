package terminals;

import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import logger.LoggerManager;
import vehicles.Vehicle;

public abstract class Terminal {
	private static int numberOfTerminals = 0;
	protected static Logger errorLogger = LoggerManager.getErrorLogger();
	
    protected TerminalStatus status;
    protected Vehicle<?> vehicleAtTerminal;
    protected int id;
    protected  ReentrantLock lock = new ReentrantLock();

    public Terminal() {
        status = TerminalStatus.AVAILABLE;
        vehicleAtTerminal = null;
        this.id = Terminal.numberOfTerminals++;
    }

    /**
     * Polls the queue of Vehicles and retrieves the next Vehicle waiting  in line;
     * @param vehicleQueue Queue of vehicles waiting to get processed at the terminal
     * @return Vehicle object that was next in line, or null if the line is empty
     */
    public Vehicle<?> takeNextVehicle(Queue<Vehicle<?>> vehicleQueue) {
    	Vehicle<?> returnedVehicle = null;
    	if(this.vehicleAtTerminal != null)
    	{
    		return this.vehicleAtTerminal; //If the vehicle slot is not empty, return that vehicle
    	}
    	
    	try
    	{
    		returnedVehicle = vehicleQueue.poll();
    		status = TerminalStatus.PROCESSING;
    	}
    	catch(Exception ex)
    	{
    		errorLogger.severe("<Error polling the Vehicle Queue>: " + ex.getMessage());
    	}
        
        if(returnedVehicle == null)
        {
        	return null;
        }
        else
        {
        	return returnedVehicle;
        }
    }
    
    public void blockTerminal()
    {
    	this.status = TerminalStatus.BLOCKED;
    }
}

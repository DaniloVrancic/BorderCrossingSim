package terminals;

import java.util.concurrent.BlockingQueue;

import passengers.Passenger;
import passengers.PunishedPassenger;
import passengers.PunishedPersonManager;
import vehicles.StoppedVehicleManager;
import vehicles.Vehicle;

/**
 * PoliceTerminal has reference to the Queue of Vehicle's
 * standing in line to get processed.
 * PoliceTerminal also contains a reference to their
 * corresponding CustomsTerminal.
 * 
 * Once a vehicle finishes being processed, the status
 * of the PoliceTerminal changes to FINISHED_AND_WAITING
 * 
 * The PoliceTerminal will wait until
 */
public abstract class PoliceTerminal extends Terminal{

	///////////////////   PASSENGER EXPLANATIONS	/////////////////////////
	private final String DRIVER_IDENTIFICATION_INVALID_EXPLANATION() 
	{
		if(this.vehicleAtTerminal != null)
		{
			return "The driver of vehicle(id): " + this.vehicleAtTerminal.getVehicleId() + "has invalid identification documents and is getting his entry cancelled.";
		}
		else
		{
			return "No vehicle in terminal to be processed";
		}
				};
	
	private static final String PASSENGER_IDENTIFICATION_INVALID_EXPLANATION(Passenger p)
	{
		return "The Passenger: " + p.getFullName() + " (PASSPORT NUMBER: " +p.document.getPassportNumber() + " ) has an invalid identification document and therefore is getting his entry cancelled.";
	}
	
	private final String QUEUE_POINTS_TO_NULL_EXPLANATION = "The queue can't be a null pointer";
	/////////////////////////////////////////////////////////////////////////
	///////////////////	VEHICLE EVICTION EXPLANATIONS ///////////////////////
	private final String VEHICLE_DRIVER_HAS_INVALID_DOCUMENT_EXPLANATION()
	{
		if(this.vehicleAtTerminal != null)
		{
			return "The vehicle(id): [ " + this.vehicleAtTerminal.getVehicleId() + " ] driver has an invalid document and therefore can't cross the border.";			
		}
		else
		{
			return "No vehicle in terminal to be processed";
		}
	}
	/////////////////////////////////////////////////////////////////////////
	
	
	protected Terminal connectedTerminal = null;
	protected BlockingQueue<Vehicle<?>> vehicleQueue = null;
	

	public PoliceTerminal(BlockingQueue<Vehicle<?>> vehicleQueue)
	{
		super();
		this.vehicleQueue = vehicleQueue;
	}
	
	public void connectTerminal(Terminal other)
	  {
	    	if(other != null)
	    	connectedTerminal = other;
	    	else
	    	 infoLogger.severe("Can't connect to other terminal that is null");
	  }

	public void setVehicleQueue(BlockingQueue<Vehicle<?>> vehicleQueue) {
		this.vehicleQueue = vehicleQueue;
	}
	
    /**
     * Polls the queue of Vehicles and retrieves the next Vehicle waiting  in line;
     * @param vehicleQueue Queue of vehicles waiting to get processed at the terminal
     * @return Vehicle object that was next in line, or null if the line is empty
     */
    public Vehicle<?> takeNextVehicle() {
    	if(this.vehicleQueue == null)
    	{
    		throw new NullPointerException(QUEUE_POINTS_TO_NULL_EXPLANATION);
    	}
    	
    	Vehicle<?> returnedVehicle = null;
    	if(this.vehicleAtTerminal != null)
    	{
    		return this.vehicleAtTerminal; //If the vehicle slot is not empty, return that vehicle
    	}
    	
    	try
    	{
    		returnedVehicle = vehicleQueue.poll();
    		status = TerminalStatus.READY_FOR_PROCESSING;
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
    } //end of takeNextVehicle (Method)
	
	public void processVehicle(int processingTime)
	{
		status = TerminalStatus.PROCESSING;
		try
		{
			System.out.println("Processing driver: " + this.vehicleAtTerminal.driver.getFullName()); //DELETE LATER, TESTING PURPOSES
			boolean driverPassed = processDriver(this.vehicleAtTerminal.driver, processingTime);
			
			if(!driverPassed)
			{
				PunishedPersonManager.addPunishment(new PunishedPassenger(this.vehicleAtTerminal.driver, DRIVER_IDENTIFICATION_INVALID_EXPLANATION(), this.vehicleAtTerminal));
				StoppedVehicleManager.addStoppedVehicle(this.vehicleAtTerminal, VEHICLE_DRIVER_HAS_INVALID_DOCUMENT_EXPLANATION());
				vehicleAtTerminal = null;
				status = TerminalStatus.AVAILABLE; //Free up the terminal from the vehicle that was being processed
				return; //No need to continue processing after this if the driver is evicted
			}
			
			processPassengers(this.vehicleAtTerminal, processingTime);
			status = TerminalStatus.FINISHED_AND_WAITING;
			if(vehicleQueue.size() > 0)
			{
				//vehicleQueue.peek().notify();				
			}
		}
		catch(InterruptedException ex)
		{
			errorLogger.severe("<Processing Vehicle error><Interrupted Exception>: " + ex.getMessage());
		}
		catch(Exception ex)
		{
			errorLogger.severe("<Processing Vehicle exception>: " + ex.getMessage());
		}
	}
	
	private boolean processDriver(Passenger driver, int processingTime) throws InterruptedException
	{
		Thread.sleep(processingTime);
		if(Math.random() <= 0.03) //a 3% chance for the driver to be incorrect (according to system requirements)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private void processPassengers(Vehicle<?> vehicle, int processingTime) throws InterruptedException
	{
		
		for(Passenger p : vehicle.passengers)
		{
			System.out.println("PROCESSING PASSENGER: " + p.getFullName()); //DELETE LATER, TESTING PURPOSES
			Thread.sleep(processingTime);
			if(Math.random() <= 0.03) // Chance of 3%
			{
				synchronized(vehicle.passengers)
				{
					PunishedPersonManager.addPunishment(new PunishedPassenger(p, PASSENGER_IDENTIFICATION_INVALID_EXPLANATION(p), this.vehicleAtTerminal));
					System.out.println("REMOVED: " + p); //REMOVE THIS LINE LATER
					vehicle.passengers.remove(p);		//Punish the passenger with Invalid documents and throw him out of the Passenger list
				}
			}
		}
	} //end of processPassengers(Vehicle<?>, int) (Method)
	
}

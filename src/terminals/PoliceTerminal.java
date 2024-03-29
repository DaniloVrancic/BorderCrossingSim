package terminals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import gui.BorderCrossingGUIController;
import passengers.Passenger;
import passengers.PunishedPassenger;
import passengers.PunishmentManager;
import vehicles.PunishedVehicle;
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
	
	
	protected BlockingQueue<Vehicle<?>> vehicleQueue = null;
	

	public PoliceTerminal(BlockingQueue<Vehicle<?>> vehicleQueue)
	{
		super();
		this.vehicleQueue = vehicleQueue;
	}
	

	public synchronized void setVehicleQueue(BlockingQueue<Vehicle<?>> vehicleQueue) {
		this.vehicleQueue = vehicleQueue;
	}
	
	public void processVehicle(int processingTime)
	{
		status = TerminalStatus.PROCESSING;
		try
		{
			boolean driverPassed = processDriver(this.vehicleAtTerminal.driver, processingTime);
			
			if(!driverPassed)
			{
				BorderCrossingGUIController.getInstance().updateRelevantEventsTextArea("PUNISHING DRIVER: " + this.vehicleAtTerminal.driver.getFullName() + "\nof vehicle (ID):" + this.vehicleAtTerminal.getVehicleId() + "\nTYPE: ( " + this.getVehicleAtTerminal().getClass().getSimpleName() + " )");
				//System.out.println("PUNISHING Driver: " + this.vehicleAtTerminal.driver.getFullName() + " of vehicle id:" + this.vehicleAtTerminal.getVehicleId() + " TYPE: (" + this.getVehicleAtTerminal().getClass().getSimpleName() + " )");
				PunishmentManager.addPunishment(new PunishedVehicle(this.vehicleAtTerminal, VEHICLE_DRIVER_HAS_INVALID_DOCUMENT_EXPLANATION())); //First add the vehicle that has been punished to the punishment map list
				PunishmentManager.addPunishment(new PunishedPassenger(this.vehicleAtTerminal.driver, DRIVER_IDENTIFICATION_INVALID_EXPLANATION(), this.vehicleAtTerminal)); //Then add the driver as the passenger to that subsection
				StoppedVehicleManager.addStoppedVehicle(this.vehicleAtTerminal, VEHICLE_DRIVER_HAS_INVALID_DOCUMENT_EXPLANATION());
				status = TerminalStatus.VEHICLE_PUNISHED; //Change the status of the terminal to indicate the vehicle didn't pass
				
				return; //No need to continue processing after this if the driver is evicted
			}
			else
			{
				status = TerminalStatus.VEHICLE_PASSED;
				
			}
			
			processPassengers(this.vehicleAtTerminal, processingTime);
			
			
			
		
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
		List<Passenger> passengersToRemove = new ArrayList<>();
		for(Passenger p : vehicle.passengers)
		{
			Thread.sleep(processingTime);
			if(Math.random() <= 0.03) // Chance of 3%
			{
				synchronized(vehicle.passengers)
				{
					PunishmentManager.addPunishment(new PunishedPassenger(p, PASSENGER_IDENTIFICATION_INVALID_EXPLANATION(p), this.vehicleAtTerminal));
					BorderCrossingGUIController.getInstance().updateRelevantEventsTextArea("PUNISHING and REMOVING passenger: " + p.getFullName() + " from VEHICLE(ID): " + this.vehicleAtTerminal.getVehicleId() + " TYPE: " + this.vehicleAtTerminal.getClass().getSimpleName());
					//System.out.println("PUNISHING AND REMOVING PASSENGER: " + p.getFullName() + " FROM VEHICLE (ID) :" + this.vehicleAtTerminal.getVehicleId() + " TYPE: " + this.vehicleAtTerminal.getClass().getSimpleName()); //REMOVE THIS LINE LATER
					passengersToRemove.add(p);		//Punish the passenger with Invalid documents and throw him out of the Passenger list
				}
			}
		}
		vehicle.passengers.removeAll(passengersToRemove);
	} //end of processPassengers(Vehicle<?>, int) (Method)
	

}

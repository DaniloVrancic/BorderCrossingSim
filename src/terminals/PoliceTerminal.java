package terminals;

import java.util.List;

import passengers.Passenger;
import passengers.PunishedPassenger;
import passengers.PunishedPersonManager;
import vehicles.StoppedVehicleManager;
import vehicles.Vehicle;

public class PoliceTerminal extends Terminal{

	///////////////////   PASSENGER EXPLANATIONS
	private final String DRIVER_IDENTIFICATION_INVALID_EXPLANATION = "The driver of vehicle(id): " + this.vehicleAtTerminal.getVehicleId() + "has invalid identification documents and is getting his entry cancelled.";
	
	private static String PASSENGER_IDENTIFICATION_INVALID_EXPLANATION(Passenger p)
	{
		return "The Passenger: " + p.getFullName() + " (PASSPORT NUMBER: " +p.document.getPassportNumber() + " ) has an invalid identification document and therefore is getting his entry cancelled.";
	}
	
	/////////////////////////////////////////////////////////////////////////
	///////////////////	VEHICLE EVICTION EXPLANATIONS ///////////////////////
	private final String VEHICLE_DRIVER_HAS_INVALID_DOCUMENT_EXPLANATION = "The vehicle(id): [ " + this.vehicleAtTerminal.getVehicleId() + " ] driver has an invalid document and therefore can't cross the border.";
	/////////////////////////////////////////////////////////////////////////
	
	public PoliceTerminal()
	{
		super();
	}
	
	public void processVehicle(int processingTime)
	{
		status = TerminalStatus.PROCESSING;
		try
		{
			boolean driverPassed = processDriver(this.vehicleAtTerminal.driver, processingTime);
			
			if(!driverPassed)
			{
				PunishedPersonManager.addPunishment(new PunishedPassenger(this.vehicleAtTerminal.driver, DRIVER_IDENTIFICATION_INVALID_EXPLANATION, this.vehicleAtTerminal));
				StoppedVehicleManager.addStoppedVehicle(this.vehicleAtTerminal, VEHICLE_DRIVER_HAS_INVALID_DOCUMENT_EXPLANATION);
				vehicleAtTerminal = null;
				status = TerminalStatus.AVAILABLE; //Free up the terminal from the vehicle that was being processed
				return; //No need to continue processing after this if the driver is evicted
			}
			
			processPassengers(this.vehicleAtTerminal, processingTime);
			status = TerminalStatus.FINISHED_AND_WAITING;
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
			Thread.sleep(processingTime);
			if(Math.random() <= 0.03) // Chance of 3%
			{
				PunishedPersonManager.addPunishment(new PunishedPassenger(p, PASSENGER_IDENTIFICATION_INVALID_EXPLANATION(p), this.vehicleAtTerminal));
				vehicle.passengers.remove(p);
			}
		}
	}
	
	
}

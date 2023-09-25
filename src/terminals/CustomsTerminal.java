package terminals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import gui.BorderCrossingGUIController;
import passengers.BusPassenger;
import passengers.Passenger;
import passengers.PunishedPassenger;
import passengers.PunishmentManager;
import vehicles.Automobile;
import vehicles.Bus;
import vehicles.StoppedVehicleManager;
import vehicles.Truck;
import vehicles.documents.CustomsDocument;
import vehicles.PunishedVehicle;

public abstract class CustomsTerminal extends Terminal{
	
	
	//////////////////////////// EXCEPTION EXPLANATIONS ////////////////////////////////
	private static final String DRIVER_ILLEGAL_LUGGAGE_EXPLANATION = "The driver has had illegal luggage detected at checkup!";
	private static final String PASSENGER_ILLEGAL_LUGGAGE_EXPLANATION = "The passenger has had illegal luggage, he is removed from the vehicle!";
	private static final String VEHICLE_OVER_DECLARED_WEIGHT_EXPLANATION = "The vehicle was over the declared cargo limit, thus denied border cross!";
	
	/**
	 * List of PoliceTerminals from where vehicles are allowed to enter this CustomsTerminal
	 */
	protected List<PoliceTerminal> watchingTerminals;
	
	public CustomsTerminal(List<PoliceTerminal> terminalsToWatch)
	{
		this.watchingTerminals = terminalsToWatch;
	}
	
	public CustomsTerminal()
	{
		
	}
	
	
	public void processVehicle(int processingTime) throws InterruptedException
	{
		
		//System.out.println("Processing vehicle: " + this.vehicleAtTerminal.getClass().getSimpleName() + " vehicle_id: " +this.vehicleAtTerminal.getVehicleId() + " ON CUSTOMS TERMINAL: " + this.id);
		if(this.vehicleAtTerminal instanceof Automobile)
		{
			processVehicleAutomobile(processingTime);
		}
		if(this.vehicleAtTerminal instanceof Bus)
		{
			processVehicleBus(processingTime);
		}
		if(this.vehicleAtTerminal instanceof Truck)
		{
			processVehicleTruck(processingTime);
		}
		if(BorderCrossingGUIController.getInstance() != null && BorderCrossingGUIController.getInstance().relevantEventsTextArea != null)
		{
			BorderCrossingGUIController.getInstance().updateRelevantEventsTextArea("Finished processing vehicle(ID): " + this.vehicleAtTerminal.getVehicleId() + " \nType: " + this.vehicleAtTerminal.getClass().getSimpleName());			
		}
		//System.out.println("Finished processing: " + this.vehicleAtTerminal.getVehicleId()); //DELETE LATER
	}
	
	private void processVehicleAutomobile(int processingTime) throws InterruptedException
	{
			Thread.sleep(processingTime); //Sleep for given time;
			this.status = TerminalStatus.VEHICLE_PASSED;
	}
	
	
	private void processVehicleBus(int processingTime) throws InterruptedException
	{
		this.status = TerminalStatus.AVAILABLE;
		Thread.sleep(processingTime);
		BusPassenger driver = (BusPassenger)this.vehicleAtTerminal.driver;
		if(driver.hasIllegalLuggage())
		{
			BorderCrossingGUIController.getInstance().updateRelevantEventsTextArea("Driver: "+ driver.getFullName() +" HAS ILLEGAL LUGGAGE AND BUS(ID): " + this.vehicleAtTerminal.getVehicleId() + "IS BEING REMOVED!");
			//System.out.println("DRIVER: "+ driver.getFullName() +" HAS ILLEGAL LUGGAGE AND BUS IS BEING REMOVED!"); //DELETE LATER

			PunishmentManager.addPunishment(new PunishedVehicle(this.vehicleAtTerminal, DRIVER_ILLEGAL_LUGGAGE_EXPLANATION));
			PunishmentManager.addPunishment(new PunishedPassenger(driver, DRIVER_ILLEGAL_LUGGAGE_EXPLANATION, this.vehicleAtTerminal));
			StoppedVehicleManager.addStoppedVehicle(this.vehicleAtTerminal, DRIVER_ILLEGAL_LUGGAGE_EXPLANATION);
			infoLogger.info("<BUS REMOVED CAUSE OF DRIVER ILLEGAL LUGGAGE> " + this.vehicleAtTerminal.getVehicleId());
			
			status = TerminalStatus.VEHICLE_PUNISHED; //Free up the terminal from the vehicle that was being processed
			return; //No need to continue processing after this if the driver is evicted
		}  //UNLESS THE DRIVER CHECK IS ALSO NECESSARY
		
		List<Passenger> passengersToRemove = new ArrayList<>();
		
		for(Passenger bp : this.vehicleAtTerminal.passengers)
		{
			Thread.sleep(processingTime);
			if(((BusPassenger) bp).hasIllegalLuggage())
			{
				synchronized(this.vehicleAtTerminal.passengers)
				{
					infoLogger.info("<PASSENGER ILLEGAL LUGGAGE AT CUSTOMS>: " + PASSENGER_ILLEGAL_LUGGAGE_EXPLANATION + "\tPassenger Name: " + bp.getFullName());
					PunishmentManager.addPunishment(new PunishedPassenger(bp, PASSENGER_ILLEGAL_LUGGAGE_EXPLANATION, this.vehicleAtTerminal));
					BorderCrossingGUIController.getInstance().updateRelevantEventsTextArea("Passenger: "+ bp.getFullName() +" has illegal luggage and will get REMOVED from Bus(ID): " + this.vehicleAtTerminal.getVehicleId());
					//System.out.println("PERSON:" + bp.getFullName() + " HAS ILLEGAL LUGGAGE AND IS GETTING THROWN OUT!"); //DELETE LATER
					passengersToRemove.add(bp);				
				}
			}
		} //CHECK EVERY PASSENGER ON THE BUS
		this.vehicleAtTerminal.passengers.removeAll(passengersToRemove);
		this.status = TerminalStatus.VEHICLE_PASSED; //If the code executed to here, then the bus has passed
	}
	
	private void processVehicleTruck(int processingTime)
	{
		boolean removed = false;
		
		Truck truckAtTerminal = (Truck)this.vehicleAtTerminal;
		if(truckAtTerminal.isDocumentationNecessary())
		{
			truckAtTerminal.customsDocument = Optional.of(new CustomsDocument());
			BorderCrossingGUIController.getInstance().updateRelevantEventsTextArea("Creating CUSTOMS_DOCUMENT for Truck(ID): " + truckAtTerminal.getVehicleId());
			//System.out.println("CREATING customs document!"); //DELETE LATER
		} //end if
		
		if(!truckAtTerminal.customsDocument.equals(Optional.empty()))
		{
			CustomsDocument cdocument = truckAtTerminal.customsDocument.get();
			
			BorderCrossingGUIController.getInstance().updateRelevantEventsTextArea("TRUCK(ID): " + truckAtTerminal.getVehicleId() + " DOCUMENT:\n" + cdocument.toString());
			//System.out.println(cdocument); //DELETE LATER
			
			int declaredWeight = cdocument.getDeclaredWeight();
			int actualWeight = cdocument.getActualWeight();
			
			if(actualWeight > declaredWeight)
			{
				BorderCrossingGUIController.getInstance().updateRelevantEventsTextArea("Truck(ID): " + truckAtTerminal.getVehicleId() + " is overweight, REMOVING from customs terminal");
				//System.out.println("TRUCK IS OVERWEIGHT, REMOVING FROM CUSTOMS TERMINALS"); //DELETE THIS LATER
				PunishmentManager.addPunishment(new PunishedVehicle(truckAtTerminal, VEHICLE_OVER_DECLARED_WEIGHT_EXPLANATION)); //Adds the punishment to be serialized
				StoppedVehicleManager.addStoppedVehicle(truckAtTerminal, VEHICLE_OVER_DECLARED_WEIGHT_EXPLANATION); //adds, kind of a text log to what happened
				infoLogger.info("<TRUCK REMOVED CAUSE OF OVERWEIGHT CARGO> " + truckAtTerminal.getVehicleId());
				removed = true;
			}//end if
			
		}//end if
		
		try
		{
			Thread.sleep(processingTime);
		}
		catch(InterruptedException ex)
		{
			errorLogger.severe("<INTERRUPTED WHILE SLEEPING>: " + ex.getMessage());
		}
		
		if(removed == true)
		{
			this.status = TerminalStatus.VEHICLE_PUNISHED;
		}
		else
		{
			this.status = TerminalStatus.VEHICLE_PASSED;			
		}
		
		
	}
}

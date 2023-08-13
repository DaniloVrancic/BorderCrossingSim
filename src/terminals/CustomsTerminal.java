package terminals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import passengers.BusPassenger;
import passengers.Passenger;
import passengers.PunishedPassenger;
import passengers.PunishedPersonManager;
import vehicles.Automobile;
import vehicles.Bus;
import vehicles.StoppedVehicleManager;
import vehicles.Truck;
import vehicles.documents.CustomsDocument;

public abstract class CustomsTerminal extends Terminal{
	
	
	//////////////////////////// EXCEPTION EXPLANATIONS ////////////////////////////////
	private static final String DRIVER_ILLEGAL_LUGGAGE_EXPLANATION = "The driver has had illegal luggage, thus the bus can not got through!";
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
	
	
	public void processVehicle(int processingTime) throws InterruptedException
	{
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
		System.out.println("Finished processing: " + this.getVehicleAtTerminal().getVehicleId()); //DELETE LATER
		this.status = TerminalStatus.FINISHED_AND_WAITING;
	}
	
	private void processVehicleAutomobile(int processingTime) throws InterruptedException
	{
			System.out.println("Processing automobile id: " + this.vehicleAtTerminal.getVehicleId()); //DELETE LATER
			Thread.sleep(processingTime); //Sleep for given time;
	}
	
	
	private void processVehicleBus(int processingTime) throws InterruptedException
	{
		System.out.println("Processing bus id: " + this.vehicleAtTerminal.getVehicleId()); //DELETE LATER
//		Thread.sleep(processingTime);
//		BusPassenger driver = (BusPassenger)this.vehicleAtTerminal.driver;
//		if(driver.hasIllegalLuggage())
//		{
//			PunishedPersonManager.addPunishment(new PunishedPassenger(driver, DRIVER_ILLEGAL_LUGGAGE_EXPLANATION, this.vehicleAtTerminal));
//			StoppedVehicleManager.addStoppedVehicle(this.vehicleAtTerminal, DRIVER_ILLEGAL_LUGGAGE_EXPLANATION);
//			infoLogger.info("<BUS REMOVED CAUSE OF DRIVER ILLEGAL LUGGAGE> " + this.vehicleAtTerminal.getVehicleId());
//			vehicleAtTerminal = null;
//			status = TerminalStatus.AVAILABLE; //Free up the terminal from the vehicle that was being processed
//			return; //No need to continue processing after this if the driver is evicted
//		}  //UNLESS THE DRIVER CHECK IS ALSO NECESSARY
		
		List<Passenger> passengersToRemove = new ArrayList<>();
		
		for(Passenger bp : this.vehicleAtTerminal.passengers)
		{
			Thread.sleep(processingTime);
			if(((BusPassenger) bp).hasIllegalLuggage())
			{
				synchronized(this.vehicleAtTerminal.passengers)
				{
					infoLogger.info("<PASSENGER ILLEGAL LUGGAGE AT CUSTOMS>: " + PASSENGER_ILLEGAL_LUGGAGE_EXPLANATION + "\tPassenger Name: " + bp.getFullName());
					PunishedPersonManager.addPunishment(new PunishedPassenger(bp, PASSENGER_ILLEGAL_LUGGAGE_EXPLANATION, this.vehicleAtTerminal));
					System.out.println("PERSON:" + bp.getFullName() + "HAS ILLEGAL LUGGAGE AND WILL GET THROWN OUT!"); //DELETE LATER
					passengersToRemove.add(bp);				
				}
			}
		}
		this.vehicleAtTerminal.passengers.removeAll(passengersToRemove);
		for(Passenger p: this.vehicleAtTerminal.passengers) //DELETE LATER
		{//DELETE LATER
			System.out.println(p);//DELETE LATER
		}//DELETE LATER
	}
	
	private void processVehicleTruck(int processingTime)
	{
		System.out.println("Processing truck id: " + this.vehicleAtTerminal.getVehicleId()); //DELETE LATER
		Truck truckAtTerminal = (Truck)this.vehicleAtTerminal;
		if(truckAtTerminal.isDocumentationNecessary())
		{
			truckAtTerminal.customsDocument = Optional.of(new CustomsDocument());
			System.out.println("CREATING customs document!"); //DELETE LATER
		}
		
		if(!truckAtTerminal.customsDocument.equals(Optional.empty()))
		{
			CustomsDocument cdocument = truckAtTerminal.customsDocument.get();
			System.out.println(cdocument); //DELETE LATER
			
			int declaredWeight = cdocument.getDeclaredWeight();
			int actualWeight = cdocument.getActualWeight();
			
			if(actualWeight > declaredWeight)
			{
				System.out.println("TRUCK IS OVERWEIGHT, REMOVING FROM TERMINALS"); //DELETE THIS LATER
				StoppedVehicleManager.addStoppedVehicle(truckAtTerminal, VEHICLE_OVER_DECLARED_WEIGHT_EXPLANATION);
				infoLogger.info("<TRUCK REMOVED CAUSE OF OVERWEIGHT CARGO> " + truckAtTerminal.getVehicleId());
			}//end if
			
		}//end if
	}
}

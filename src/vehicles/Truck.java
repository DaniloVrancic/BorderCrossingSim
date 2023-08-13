package vehicles;


import exceptions.IllegalNumberOfPassengers;

import java.io.Serializable;
import java.util.Optional;
import passengers.Passenger;
import util.random.RandomGenerator;
import vehicles.documents.CustomsDocument;

public class Truck extends Vehicle<Passenger> implements Serializable{

	private static int MAX_TRUCK_CAPACITY = 3;
	
	private boolean documentationNecessary;
	public Optional<CustomsDocument> customsDocument = Optional.empty(); //Will be filled by the CustomsTerminal
	
	public Truck() {
		super();
		
		try {
			int randomGeneratedNumOfPassengers = RandomGenerator.generateInRange(1, MAX_TRUCK_CAPACITY);
			this.setNumOfPassengers(randomGeneratedNumOfPassengers);
			fillTruckWithPassengers(randomGeneratedNumOfPassengers); //Adds the driver and the Passengers
		} catch (IllegalNumberOfPassengers ex) {
			errorLogger.severe(ex.getMessage());
		} catch (Exception ex)
		{
			errorLogger.severe(ex.getMessage());
		}
		
		this.documentationNecessary = (Math.random() < 0.5) ? true : false; //50% chance that declaration will be necessary
		
		
		infoLogger.info("Truck successfully created.");
		
	}

	
	@Override
	public void setNumOfPassengers(int numOfPassengers) throws IllegalNumberOfPassengers 
    {
       this.setNumOfPassengers(numOfPassengers, MAX_TRUCK_CAPACITY);
    }
	
	private void fillTruckWithPassengers(int randomGeneratedNumOfPassengers) {
		this.driver = new Passenger(Vehicle.generator);
		--randomGeneratedNumOfPassengers; //Remove one Passenger after adding the driver
		
		while(randomGeneratedNumOfPassengers > 0) //for each number, generate another Passenger in the Passenger list of the Truck
		{
			this.passengers.add(new Passenger(Vehicle.generator));
			--randomGeneratedNumOfPassengers;
		}
	}
	
	public boolean isDocumentationNecessary()
	{
		return this.documentationNecessary;
	}
}

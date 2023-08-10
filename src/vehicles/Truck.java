package vehicles;


import exceptions.IllegalNumberOfPassengers;
import java.util.Optional;
import passengers.Passenger;
import util.random.RandomGenerator;
import vehicles.documents.CustomsDocument;

public class Truck extends Vehicle<Passenger>{

	private static int MAX_TRUCK_CAPACITY = 3;
	
	boolean documentationNecessary;
	public Optional<CustomsDocument> customsDocument; //Will be filled by the CustomsTerminal if necessary
	
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
		
		this.documentationNecessary = (Math.random() < 0.5) ? true : false;
		
		
		infoLogger.info("Truck successfully created.");
		
	}

	
	@Override
	public void setNumOfPassengers(int numOfPassengers) throws IllegalNumberOfPassengers 
    {
        if (numOfPassengers < 1 || numOfPassengers > MAX_TRUCK_CAPACITY) {
            if (numOfPassengers < 1) {
                String errorMessage = "Number of passengers needs to be at least 1";
                throw new IllegalNumberOfPassengers(errorMessage);
            } else if (numOfPassengers > MAX_TRUCK_CAPACITY) {
                String errorMessage = "Number of passengers can not be more than " + MAX_TRUCK_CAPACITY;
                throw new IllegalNumberOfPassengers(errorMessage);
            }
        } else {
            this.numOfPassengers = numOfPassengers;
        }
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
}

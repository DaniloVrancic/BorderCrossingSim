package vehicles;

import exceptions.IllegalNumberOfPassengers;
import passengers.Passenger;
import util.random.RandomGenerator;

public class Automobile extends Vehicle<Passenger>{

	private static int MAX_AUTOMOBILE_CAPACITY = 5;
	public Automobile() {
		super();
		
		try {
			int randomGeneratedNumOfPassengers = RandomGenerator.generateInRange(1, MAX_AUTOMOBILE_CAPACITY);
			this.setNumOfPassengers(randomGeneratedNumOfPassengers);
			fillAutomobileWithPassengers(randomGeneratedNumOfPassengers); //Fills the car with Passenger's
		} catch (IllegalNumberOfPassengers ex) {
			errorLogger.severe(ex.getMessage());
		}
		catch (Exception ex)
		{
			errorLogger.severe(ex.getMessage());
		}
		infoLogger.info("Automobile successfully created.");
		
		
	}

	
	@Override
	public void setNumOfPassengers(int numOfPassengers) throws IllegalNumberOfPassengers 
    {
        if (numOfPassengers < 1 || numOfPassengers > MAX_AUTOMOBILE_CAPACITY) {
            if (numOfPassengers < 1) {
                String errorMessage = "Number of passengers needs to be at least 1";
                throw new IllegalNumberOfPassengers(errorMessage);
            } else if (numOfPassengers > MAX_AUTOMOBILE_CAPACITY) {
                String errorMessage = "Number of passengers can not be more than " + MAX_AUTOMOBILE_CAPACITY;
                throw new IllegalNumberOfPassengers(errorMessage);
            }
        } else {
            this.numOfPassengers = numOfPassengers;
        }
    }
	
	private void fillAutomobileWithPassengers(int randomGeneratedNumOfPassengers) {
		this.driver = new Passenger(Vehicle.generator);
		--randomGeneratedNumOfPassengers; //Remove one Passenger after adding the driver
		
		while(randomGeneratedNumOfPassengers > 0) //for each number, generate another Passenger in the Passenger list of the Automobile
		{
			this.passengers.add(new Passenger(Vehicle.generator));
			--randomGeneratedNumOfPassengers;
		}
	}
}

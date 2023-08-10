package vehicles;

import exceptions.IllegalNumberOfPassengers;
import passengers.BusPassenger;
import util.random.RandomGenerator;

public class Bus extends Vehicle<BusPassenger>{

	private static int MAX_BUS_CAPACITY = 52;
	
	public Bus() {
		super();
		
		try {
			int randomGeneratedNumOfPassengers = RandomGenerator.generateInRange(1, MAX_BUS_CAPACITY);
			this.setNumOfPassengers(randomGeneratedNumOfPassengers);
			fillBusWithPassengers(randomGeneratedNumOfPassengers); //Fills the car with Passenger's
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
       this.setNumOfPassengers(numOfPassengers, MAX_BUS_CAPACITY);
    }
	
	private void fillBusWithPassengers(int randomGeneratedNumOfPassengers) {
		this.driver = new BusPassenger(Vehicle.generator);
		--randomGeneratedNumOfPassengers; //Remove one Passenger after adding the driver
		
		while(randomGeneratedNumOfPassengers > 0) //for each number, generate another Passenger in the Passenger list of the Automobile
		{
			this.passengers.add(new BusPassenger(Vehicle.generator));
			--randomGeneratedNumOfPassengers;
		}
	}
}

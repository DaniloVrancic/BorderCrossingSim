package vehicles;

import java.io.Serializable;
import java.util.List;

import exceptions.IllegalNumberOfPassengers;
import passengers.Passenger;
import terminals.PoliceTerminal;
import terminals.PoliceTerminalForOthers;
import terminals.managers.PoliceTerminalsManager;
import util.random.RandomGenerator;

public class Automobile extends Vehicle<Passenger> implements Serializable{

	private static int MAX_AUTOMOBILE_CAPACITY = 5;
	public Automobile() {
		super(PoliceTerminalsManager.availablePoliceTerminalsForOthers);
		
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
       this.setNumOfPassengers(numOfPassengers, MAX_AUTOMOBILE_CAPACITY);
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
	
	
	
	@Override
	public void run() {
	    try {
	    	List<PoliceTerminal> availableTerminals = PoliceTerminalsManager.availablePoliceTerminalsForOthers;
	        PoliceTerminalForOthers assignedTerminal = null;
	        while (assignedTerminal == null) {
	            synchronized (availableTerminals) {
	                for (PoliceTerminal terminal : availableTerminals) {
	                    if (terminal.isAvailable() && terminal instanceof PoliceTerminalForOthers) {
	                        assignedTerminal = (PoliceTerminalForOthers) terminal;
	                        break;
	                    }
	                }
	                if (assignedTerminal == null) {
	                	availableTerminals.wait();
	                }
	            }
	        }

	        // Assign the vehicle to the terminal
	        synchronized (assignedTerminal) {
	            assignedTerminal.setVehicleAtTerminal(this);
	            //availableTerminals.remove(assignedTerminal); // Remove terminal from available list
	        }

	        assignedTerminal.processVehicle();
	        assignedTerminal.release();

	        synchronized (availableTerminals) {
	            assignedTerminal.setVehicleAtTerminal(null);
	          //  availableTerminals.add(assignedTerminal); // Return the terminal to the available list when processing is done
	            availableTerminals.notifyAll();
	        }
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}



}

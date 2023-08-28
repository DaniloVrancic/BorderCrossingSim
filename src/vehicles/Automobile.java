package vehicles;

import java.io.Serializable;
import java.util.List;

import exceptions.IllegalNumberOfPassengers;
import passengers.Passenger;
import terminals.CustomsTerminal;
import terminals.CustomsTerminalForOthers;
import terminals.PoliceTerminal;
import terminals.PoliceTerminalForOthers;
import terminals.managers.CustomsTerminalsManager;
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
	    	List<PoliceTerminal> availablePoliceTerminals = PoliceTerminalsManager.availablePoliceTerminalsForOthers;
	        PoliceTerminalForOthers assignedPoliceTerminal = null;
	        while (assignedPoliceTerminal == null) {
	            synchronized (availablePoliceTerminals) {
	                for (PoliceTerminal terminal : availablePoliceTerminals) {
	                    if (terminal.isAvailable() && terminal instanceof PoliceTerminalForOthers) //if it is available, the object will lock
	                    {
	                    	assignedPoliceTerminal = (PoliceTerminalForOthers) terminal;
	                        break;
	                    }
	                }
	                if (assignedPoliceTerminal == null) {
	                	availablePoliceTerminals.wait();
	                }
	            }
	        }

	        // Assign the vehicle to the terminal
	        synchronized (assignedPoliceTerminal) {
	        	assignedPoliceTerminal.setVehicleAtTerminal(this);
	            //availableTerminals.remove(assignedTerminal); // Remove terminal from available list
	        }

	        assignedPoliceTerminal.processVehicle();
	        assignedPoliceTerminal.release(); //release the lock after isAvailable locks it once it returns true

	        if(assignedPoliceTerminal.getVehicleAtTerminal() == null)
	        {
	        	synchronized (availablePoliceTerminals) {
     	        	assignedPoliceTerminal.setVehicleAtTerminal(null);
     	        	//availablePoliceTerminals.add(assignedPoliceTerminal); // Return the terminal to the available list when processing is done
     	        	availablePoliceTerminals.notifyAll();
	        		}
     	        	return;
	        } //If the vehicle got thrown out, the vehicle at terminal will be set to null, and that will be a flag that no more processing is necessary
	        
	        
	      //CUSTOMS PROCESSING >>
	        List<CustomsTerminal> availableCustomsTerminals = CustomsTerminalsManager.availableCustomsTerminalsForOthers;
	        CustomsTerminalForOthers assignedCustomsTerminal = null;
	        while (assignedCustomsTerminal == null) {
	            synchronized (availableCustomsTerminals) {
	                for (CustomsTerminal terminal : availableCustomsTerminals) {
	                    if (terminal.isAvailable() && terminal instanceof CustomsTerminalForOthers) //if it is available, the object will lock
	                    {
	                    	assignedCustomsTerminal = (CustomsTerminalForOthers) terminal;
	                    	synchronized (availablePoliceTerminals) {
	            	        	assignedPoliceTerminal.setVehicleAtTerminal(null);
	            	          //  availableTerminals.add(assignedTerminal); // Return the terminal to the available list when processing is done
	            	            availablePoliceTerminals.notifyAll();
	            	        }
	                        break;
	                    }
	                }
	                if (assignedCustomsTerminal == null) {
	                	availableCustomsTerminals.wait();
	                }
	            }
	        }
	            
	             // Assign the vehicle to the terminal
	    	        synchronized (assignedCustomsTerminal) {
	    	        	assignedCustomsTerminal.setVehicleAtTerminal(this);
	    	        }
	    	        
	    	        assignedCustomsTerminal.processVehicle();
	    	        assignedCustomsTerminal.release();
	            
	            synchronized (availableCustomsTerminals) {
		        	assignedCustomsTerminal.setVehicleAtTerminal(null);
		            availableCustomsTerminals.notifyAll();
		        }
	        
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}



}

package vehicles;

import java.io.Serializable;
import java.util.List;

import exceptions.IllegalNumberOfPassengers;
import passengers.BusPassenger;
import terminals.CustomsTerminal;
import terminals.CustomsTerminalForOthers;
import terminals.PoliceTerminal;
import terminals.PoliceTerminalForOthers;
import terminals.managers.CustomsTerminalsManager;
import terminals.managers.PoliceTerminalsManager;
import util.random.RandomGenerator;

public class Bus extends Vehicle<BusPassenger> implements Serializable{

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
	
	@Override
	public void run() {
	    try {
	    	List<PoliceTerminal> availablePoliceTerminals = PoliceTerminalsManager.availablePoliceTerminalsForOthers;
	        PoliceTerminalForOthers assignedPoliceTerminal = null;
	        while (assignedPoliceTerminal == null) {
	            synchronized (availablePoliceTerminals) {
	                for (PoliceTerminal terminal : availablePoliceTerminals) {
	                    if (terminal.isAvailable() && terminal instanceof PoliceTerminalForOthers) {
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
	        assignedPoliceTerminal.release();

	        /////////////////////////////////////////////
	        ///MISSING PARTS HERE OF MECHANISM TO MOVE TO CUSTOMS AND GET PROCESSED THERE NEXT
	        /////////////////////////////////////////////
	        
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
	             	        	//availablePoliceTerminals.add(assignedPoliceTerminal); // Return the terminal to the available list when processing is done
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

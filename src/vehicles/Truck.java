package vehicles;


import exceptions.IllegalNumberOfPassengers;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import passengers.Passenger;
import terminals.CustomsTerminal;
import terminals.CustomsTerminalForOthers;
import terminals.CustomsTerminalForTrucks;
import terminals.PoliceTerminal;
import terminals.PoliceTerminalForOthers;
import terminals.PoliceTerminalForTrucks;
import terminals.managers.CustomsTerminalsManager;
import terminals.managers.PoliceTerminalsManager;
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
	
	@Override
	public void run() {
	    try {
	    	List<PoliceTerminal> availablePoliceTerminals = PoliceTerminalsManager.availablePoliceTerminalsForTrucks;
	        PoliceTerminalForTrucks assignedPoliceTerminal = null;
	        while (assignedPoliceTerminal == null) {
	            synchronized (availablePoliceTerminals) {
	                for (PoliceTerminal terminal : availablePoliceTerminals) {
	                    if (terminal.isAvailable() && terminal instanceof PoliceTerminalForTrucks) {
	                        assignedPoliceTerminal = (PoliceTerminalForTrucks) terminal;
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
	            assignedPoliceTerminal.setVehicleAndRemoveFromQueue();
	          //  availableTerminals.remove(assignedTerminal); // Remove terminal from available list
	        }

	        assignedPoliceTerminal.processVehicle();
	        assignedPoliceTerminal.release();

	        if(assignedPoliceTerminal.getVehicleAtTerminal() == null)
	        {
	        	synchronized (availablePoliceTerminals) {
     	        	assignedPoliceTerminal.setVehicleAtTerminal(null);
     	        	//availablePoliceTerminals.add(assignedPoliceTerminal); // Return the terminal to the available list when processing is done
     	        	availablePoliceTerminals.notifyAll();
	        		}
     	        	return;
	        } //If the vehicle got thrown out, the vehicle at terminal will be set to null, and that will be a flag that no more processing is necessary
	        synchronized (availablePoliceTerminals) {
	        availablePoliceTerminals.notifyAll();
	        }
	        	//NEED TO SET TO NULL VEHICLES IN POLICE TERMINAL ONCE THEY ARE VALID AND ASSIGNED TO A CUSTOMS TERMINAL!!!!!!!!!!!!
	        //CUSTOMS PROCESSING >>
	        List<CustomsTerminal> availableCustomsTerminals = CustomsTerminalsManager.availableCustomsTerminalsForTrucks;
	        CustomsTerminalForTrucks assignedCustomsTerminal = null;
	        while (assignedCustomsTerminal == null) {
	            synchronized (availableCustomsTerminals) {
	                for (CustomsTerminal terminal : availableCustomsTerminals) {
	                    if (terminal.isAvailable() && terminal instanceof CustomsTerminalForTrucks) //if it is available, the object will lock
	                    {
	                    	assignedCustomsTerminal = (CustomsTerminalForTrucks) terminal;
	            	        synchronized (availablePoliceTerminals) {
	            	            assignedPoliceTerminal.setVehicleAtTerminal(null);
	            	         //   availableTerminals.add(assignedTerminal); // Return the terminal to the available list when processing is done
	            	            availablePoliceTerminals.notify();
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

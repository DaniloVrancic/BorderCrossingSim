package vehicles;

import java.io.Serializable;
import java.util.List;

import exceptions.IllegalNumberOfPassengers;
import gui.BorderCrossingGUIController;
import javafx.scene.paint.Color;
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
	                    if (terminal instanceof PoliceTerminalForOthers && terminal.isAvailable()) //if it is available, the object will lock
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
	        	assignedPoliceTerminal.setVehicleAndRemoveFromQueue();
	        }

	        assignedPoliceTerminal.processVehicle();

	        if(assignedPoliceTerminal.getVehicleAtTerminal() == null) //Condition is true if the vehicle got thrown out, rejected at the terminal
	        {
	        	synchronized (availablePoliceTerminals) {
	        		BorderCrossingGUIController.colorPaneofTerminal(assignedPoliceTerminal, Color.RED);
	        		try
	        		{
	        			Thread.sleep(750);
	        		}
	        		catch(InterruptedException ex)
	        		{
	        			errorLogger.severe(ex.getMessage());
	        		}
     	        	assignedPoliceTerminal.setVehicleAtTerminal(null);
     	        	assignedPoliceTerminal.release(); //release the lock after isAvailable locks it once it returns true
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
	                    if (terminal instanceof CustomsTerminalForOthers && terminal.isAvailable()) //if it is available, the object will lock
	                    {
	                    	assignedCustomsTerminal = (CustomsTerminalForOthers) terminal;
	                    	synchronized (availablePoliceTerminals) {
	            	        	assignedPoliceTerminal.setVehicleAtTerminal(null);
	            	        	assignedPoliceTerminal.release(); //release the lock after isAvailable locks it once it returns true
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
	            
	            synchronized (availableCustomsTerminals) {
		        	assignedCustomsTerminal.setVehicleAtTerminal(null);
		        	assignedCustomsTerminal.release();
		            availableCustomsTerminals.notifyAll();
		        }
	        
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}



}

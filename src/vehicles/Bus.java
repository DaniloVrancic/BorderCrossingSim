package vehicles;

import java.io.Serializable;
import java.util.List;

import exceptions.IllegalNumberOfPassengers;
import gui.BorderCrossingGUIController;
import javafx.scene.paint.Color;
import passengers.BusPassenger;
import terminals.CustomsTerminal;
import terminals.CustomsTerminalForOthers;
import terminals.PoliceTerminal;
import terminals.PoliceTerminalForOthers;
import terminals.TerminalStatus;
import terminals.managers.CustomsTerminalsManager;
import terminals.managers.PoliceTerminalsManager;
import util.random.RandomGenerator;

public class Bus extends Vehicle<BusPassenger> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8419859775974951030L;
	
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
	        	assignedPoliceTerminal.setVehicleAndRemoveFromQueue();
	        	BorderCrossingGUIController.listViewNeedsRefresh = true;
	        	BorderCrossingGUIController.terminalsNeedRefresh = true;
	        	assignedPoliceTerminal.waitWhileBlocked();
	        	if(this.isPaused)
	        	{
	        		BorderCrossingGUIController.listViewNeedsRefresh = true;
		            BorderCrossingGUIController.terminalsNeedRefresh = true;
	        		this.waitVehicle();						    	        		
	        	}
	        	assignedPoliceTerminal.processVehicle();
	        	if(this.isPaused)
	        	{
	        		BorderCrossingGUIController.listViewNeedsRefresh = true;
		            BorderCrossingGUIController.terminalsNeedRefresh = true;
	        		this.waitVehicle();						    	        		
	        	}
	        	assignedPoliceTerminal.waitWhileBlocked();
	        }


	        
	        if(assignedPoliceTerminal.getStatus() == TerminalStatus.VEHICLE_PUNISHED) //This happens if the Vehicle was punished
	        {
	        	synchronized (availablePoliceTerminals) {
	        		BorderCrossingGUIController.colorPaneofTerminal(assignedPoliceTerminal, Color.RED);
	        		try
	        		{
	        			Thread.sleep(TIME_TO_WAIT_AFTER_PUNISHMENT);
	        		}
	        		catch(InterruptedException ex)
	        		{
	        			errorLogger.severe(ex.getMessage());
	        		}
	        		assignedPoliceTerminal.setStatus(TerminalStatus.AVAILABLE);
     	        	assignedPoliceTerminal.setVehicleAtTerminal(null);
     	        	BorderCrossingGUIController.terminalsNeedRefresh = true;
     	        	assignedPoliceTerminal.release();
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
	                    		assignedPoliceTerminal.setStatus(TerminalStatus.AVAILABLE);
	             	        	assignedPoliceTerminal.setVehicleAtTerminal(null);
	             	        	BorderCrossingGUIController.terminalsNeedRefresh = true;
	             	        	assignedPoliceTerminal.release();
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
	    	        	BorderCrossingGUIController.terminalsNeedRefresh = true;
	    	        }
	    	        
	    	        if(this.isPaused)
	    	        {
	    	        	BorderCrossingGUIController.listViewNeedsRefresh = true;
	    	            BorderCrossingGUIController.terminalsNeedRefresh = true;
	    	        	this.waitVehicle();						    	        		
	    	        }
	    	        synchronized (assignedCustomsTerminal) {
						assignedCustomsTerminal.waitWhileBlocked();
					}
	    	        
	    	        assignedCustomsTerminal.processVehicle();
	    	        if(this.isPaused)
		        	{
		        		this.waitVehicle();						    	        		
		        	}
	    	        synchronized (assignedCustomsTerminal) {
	    	        	BorderCrossingGUIController.listViewNeedsRefresh = true;
	    	            BorderCrossingGUIController.terminalsNeedRefresh = true;
						assignedCustomsTerminal.waitWhileBlocked();
					}
	            
	            synchronized (availableCustomsTerminals) {
	            	if(assignedCustomsTerminal.getStatus().equals(TerminalStatus.VEHICLE_PUNISHED))
	            	{
	            		BorderCrossingGUIController.colorPaneofTerminal(assignedCustomsTerminal, Color.RED);
	            		try
	            		{
	            			Thread.sleep(TIME_TO_WAIT_AFTER_PUNISHMENT);
	            		}
	            		catch(InterruptedException ex)
	            		{
	            			errorLogger.severe("<WAITING INTERRUPTED ERROR>: " + ex.getMessage());
	            		}
	            	}
	            	assignedCustomsTerminal.setStatus(TerminalStatus.AVAILABLE);
		        	assignedCustomsTerminal.setVehicleAtTerminal(null);
		        	this.setPassed(true);
		        	BorderCrossingGUIController.terminalsNeedRefresh = true;
		        	assignedCustomsTerminal.release();
		            availableCustomsTerminals.notify();
		        }
	        
	        
	    } catch (InterruptedException e) {
	    	errorLogger.severe("<INTERRUPTED EXCEPTION OCCURED>: " + e.getLocalizedMessage());
	        e.printStackTrace();
	    }
	}
}
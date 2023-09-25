package vehicles;


import exceptions.IllegalNumberOfPassengers;
import gui.BorderCrossingGUIController;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import passengers.Passenger;
import terminals.CustomsTerminal;
import terminals.CustomsTerminalForTrucks;
import terminals.PoliceTerminal;
import terminals.PoliceTerminalForTrucks;
import terminals.TerminalStatus;
import terminals.managers.CustomsTerminalsManager;
import terminals.managers.PoliceTerminalsManager;
import util.random.RandomGenerator;
import vehicles.documents.CustomsDocument;

public class Truck extends Vehicle<Passenger> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6217013059556749987L;


	private static int MAX_TRUCK_CAPACITY = 3;
	
	
	private boolean documentationNecessary;
	transient public Optional<CustomsDocument> customsDocument = Optional.empty(); //Will be filled by the CustomsTerminal
	
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
	                    if (terminal instanceof PoliceTerminalForTrucks && terminal.isAvailable())
	                    {
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
	            BorderCrossingGUIController.listViewNeedsRefresh = true;
	            BorderCrossingGUIController.terminalsNeedRefresh = true;
	            if(this.isPaused)
	        	{
	            	BorderCrossingGUIController.listViewNeedsRefresh = true;
		            BorderCrossingGUIController.terminalsNeedRefresh = true;
	        		this.waitVehicle();						    	        		
	        	}
	            assignedPoliceTerminal.waitWhileBlocked();
	            assignedPoliceTerminal.processVehicle();
	            if(this.isPaused)
	        	{
	            	BorderCrossingGUIController.listViewNeedsRefresh = true;
		            BorderCrossingGUIController.terminalsNeedRefresh = true;
	        		this.waitVehicle();						    	        		
	        	}
	            assignedPoliceTerminal.waitWhileBlocked();
	        }
	        //assignedPoliceTerminal.processVehicle(); WAS HERE FOR ALL 3

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
     	        	availablePoliceTerminals.notify();
	        		}
     	        	return;
	        } //If the vehicle got thrown out, the vehicle at terminal will be set to null, and that will be a flag that no more processing is necessary
	        
	        /////////////////////////////////////////////////////////////////////////////////////////////
	        //CUSTOMS PROCESSING >>
	        List<CustomsTerminal> availableCustomsTerminals = CustomsTerminalsManager.availableCustomsTerminalsForTrucks;
	        CustomsTerminalForTrucks assignedCustomsTerminal = null;
	        while (assignedCustomsTerminal == null) {
	            synchronized (availableCustomsTerminals) {
	                for (CustomsTerminal terminal : availableCustomsTerminals) {
	                    if (terminal instanceof CustomsTerminalForTrucks && terminal.isAvailable()) //if it is available, the object will lock
	                    {
	                    	assignedCustomsTerminal = (CustomsTerminalForTrucks) terminal;
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
	    	        		BorderCrossingGUIController.listViewNeedsRefresh = true;
	    		            BorderCrossingGUIController.terminalsNeedRefresh = true;
	    	        		this.waitVehicle();						    	        		
	    	        	}
	    	        	synchronized (assignedCustomsTerminal) {
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
	            	this.setPassed(true); //set the passed flag of the vehicle to true
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
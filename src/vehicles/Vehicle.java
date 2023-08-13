package vehicles;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import exceptions.IllegalNumberOfPassengers;
import logger.LoggerManager;
import passengers.Passenger;
import terminals.PoliceTerminal;
import terminals.PoliceTerminalForOthers;
import terminals.PoliceTerminalForTrucks;
import terminals.Terminal;
import terminals.TerminalStatus;
import util.random.IdentificationGenerator;

/**
 * An abstract class that serves as a core for all Vehicle subtypes.
 * Vehicle movement logic is implemented within the vehicle itself, therefore it
 * extends the Thread class.
 * 
 * @param <T> the type of passengers that the vehicle can accommodate, which can be either 'Passenger' or 'BusPassenger'.
 * 
 * @param infoLogger a logger object for important information that will need to be printed out.
 * @param errorLogger a logger object for errors that will need to be printed out when they occur.
 * 
 * @param capacity represents the maximum number of passengers within the vehicle (including the driver).
 * @param driver the passenger type representing the driver of the vehicle.
 * @param passengers a list containing instances of the passenger type representing all passengers aboard the vehicle (excluding the driver).
 */
abstract public class Vehicle<T extends Passenger> extends Thread {
    //////////////////////// FIELDS /////////////////////////////
    protected static final Logger infoLogger = LoggerManager.getInfoLogger();
    protected static final Logger errorLogger = LoggerManager.getErrorLogger();

    protected int numOfPassengers;

    public T driver;
    public List<T> passengers;
    
    private static int numberOfVehiclesCreated = 0;
    protected int id;
    

    public int getVehicleId() {
		return id;
	}

	protected static IdentificationGenerator generator = new IdentificationGenerator(); //Used for the generation of random Passengers

    ///////////////////////////////////////////////////////////////

    public Vehicle()
    {
    	passengers = new LinkedList<T>();
    	this.id = ++numberOfVehiclesCreated;
    }
    
    public Vehicle(int numOfPassengers) throws IllegalNumberOfPassengers
    {
    	generator = new IdentificationGenerator();
    	passengers = new LinkedList<T>();
    	this.setNumOfPassengers(numOfPassengers);
    	this.id = ++numberOfVehiclesCreated;
    }
    /**
     * Sets the number of passengers in the vehicle.
     * 
     * @param numOfPassengers the maximum number of passengers the vehicle can accommodate.
     * @throws IllegalNumberOfPassengers if the specified capacity is not within the valid range.
     */
    abstract public void setNumOfPassengers(int numOfPassengers) throws IllegalNumberOfPassengers;

	/**
	 * Just a setter class that is used to check if the set value is within
	 * the allowed limits
	 * @param numOfPassengers
	 * @param MAX_NUM_OF_PASSENGERS Every subtype has their own Max limit of Passenger's they can take
	 * @throws IllegalNumberOfPassengers
	 */
	protected void setNumOfPassengers(int numOfPassengers, int MAX_NUM_OF_PASSENGERS) throws IllegalNumberOfPassengers 
    {
        if (numOfPassengers < 1 || numOfPassengers > MAX_NUM_OF_PASSENGERS) {
            if (numOfPassengers < 1) {
                String errorMessage = "Number of passengers needs to be at least 1";
                throw new IllegalNumberOfPassengers(errorMessage);
            } else if (numOfPassengers > MAX_NUM_OF_PASSENGERS) {
                String errorMessage = "Number of passengers can not be more than " + MAX_NUM_OF_PASSENGERS;
                throw new IllegalNumberOfPassengers(errorMessage);
            }
        } else {
            this.numOfPassengers = numOfPassengers;
        }
    }
    
    /**
     * Gets the number of passengers currently in the vehicle.
     * 
     * @return the maximum number of passengers the vehicle can accommodate.
     */
    public int getNumOfPassengers() {
        return this.numOfPassengers;
    }
    
    @Override
    public String toString()
    {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append(this.getClass().getName());
    	sb.append(" :: ");
    	sb.append("Number of Passengers: ");
    	sb.append(this.numOfPassengers);
    	sb.append("\tvehicle.id = ");
    	sb.append(this.getVehicleId());
    	return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vehicle<?> otherVehicle = (Vehicle<?>) obj;
        return Objects.equals(driver, otherVehicle.driver) &&
               Objects.equals(passengers, otherVehicle.passengers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driver, passengers);
    }
    
    @Override
    public void run()
    {
    	
    }
    
    
    public void scheduleMove(PoliceTerminal[] terminals)
    {
    	while(true)
    	{
    		
    	for(PoliceTerminal t : terminals)
    	{
    		if(t.getStatus() == TerminalStatus.AVAILABLE)
    		{
    			if(t instanceof PoliceTerminalForOthers && (this instanceof Automobile || this instanceof Bus)){
    				t.work(t.takeNextVehicle());
    				return;
    			}
    			else if(t instanceof PoliceTerminalForTrucks && (this instanceof Truck))
    			{
    				t.work(t.takeNextVehicle());
    				return;
    			}
    		}    		
    	} //end of for
    	try {
    		wait();
    	} catch (InterruptedException e) {
    		errorLogger.severe(e.getMessage());
    	}
    	}//end of infinite while
    }//end of move (Method)
}

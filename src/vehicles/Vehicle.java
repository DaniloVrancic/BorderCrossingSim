package vehicles;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import custom_interfaces.Punishable;
import exceptions.IllegalNumberOfPassengers;
import logger.LoggerManager;
import passengers.Passenger;
import terminals.PoliceTerminal;
import terminals.PoliceTerminalForOthers;
import terminals.PoliceTerminalForTrucks;
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
abstract public class Vehicle<T extends Passenger> extends Thread implements Serializable{
    //////////////////////// FIELDS /////////////////////////////
    protected static final Logger infoLogger = LoggerManager.getInfoLogger();
    protected static final Logger errorLogger = LoggerManager.getErrorLogger();
    ////////////////////// LIST OF POLICE TERMINALS THAT WORK WITH THE VEHICLES
    transient protected List<PoliceTerminal> availablePoliceTerminals;
    ///////////////////////////////////////////////////////////////////////////

    transient protected int numOfPassengers;

    transient public T driver;
    transient public List<T> passengers;
    
    protected int id;
    protected boolean passed;
    
    private static int numberOfVehiclesCreated = 0;

    public int getVehicleId() {
		return id;
	}

	protected static IdentificationGenerator generator = new IdentificationGenerator(); //Used for the generation of random Passengers
	protected static int TIME_TO_WAIT_AFTER_PUNISHMENT = 750;
    ///////////////////////////////////////////////////////////////

    public Vehicle()
    {
    	passengers = new LinkedList<T>();
    	this.id = ++numberOfVehiclesCreated;
    	this.passed = false;
    }
    
    /**
     * With this, it is enabled that every vehicles 'sees' which terminals are available
     * @param availableTerminals
     */
    public Vehicle(List<PoliceTerminal> availableTerminals) {
        this.availablePoliceTerminals = availableTerminals;
        passengers = new LinkedList<T>();
        this.id = ++numberOfVehiclesCreated;
        this.passed = false;
    }

    
    public Vehicle(int numOfPassengers) throws IllegalNumberOfPassengers
    {
    	generator = new IdentificationGenerator();
    	passengers = new LinkedList<T>();
    	this.setNumOfPassengers(numOfPassengers);
    	this.id = ++numberOfVehiclesCreated;
    	this.passed = false;
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
    	
    	sb.append("TYPE: "); sb.append(this.getClass().getSimpleName());
    	sb.append("\n");
    	sb.append("ID: "); sb.append(this.getVehicleId());
    	sb.append("\n");
    	sb.append("Number of Passengers: "); sb.append(this.passengers.size() + 1);
    	sb.append("\n-------------\n");
    	sb.append("PASSENGERS INFO:");
    	sb.append("\n==========\n");
    	sb.append("DRIVER:\n");
    	sb.append(this.driver);
    	if(this.numOfPassengers > 1)
    	{
    		sb.append("OTHER PASSENGERS: \n");
    		for(Passenger p : this.passengers)
    		{
    			sb.append("\n==========\n");
    			sb.append(p);
    		}    		
    	}
    	return sb.toString();
    }
    
    public boolean isPassed()
    {
    	return this.passed;
    }
    
    public void setPassed(boolean passed)
    {
    	this.passed = passed;
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
    
    
    
   

    
    
}

package passengers;

import java.io.Serializable;

import vehicles.Vehicle;

public class PunishedPassenger extends Passenger implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2673891012650992190L;
	//////////////////////////////////////
	String explanation;
	Vehicle<?> vehicleOfPunishedPerson;
	//////////////////////////////////////

	
    public PunishedPassenger(Passenger p) {
    	super();
        this.fullName = p.getFullName(); // Copy the fullName from p
        this.document = p.document;
        this.explanation = ""; // Initialize explanation (you can set this later if needed)
        this.vehicleOfPunishedPerson = null; // Initialize vehicleOfPunishedPerson
    }
    
    /**
     * Copy constructor that takes the original Passenger and copies his data into
     * PunishedPassenger object together with
     * 
     * @param p The Passenger that was originally punished
     * @param explanation The explanation of why the Passenger was punished
     * @param vehicleOfPunishedPerson The vehicle in which the Passenger was located when he was punished (Necessary for later GUI documentation)
     */
    public PunishedPassenger(Passenger p, String explanation, Vehicle<?> vehicleOfPunishedPerson) {
    	super();
    	this.document = p.document;	// Copy the reference from the punished Passenger
        this.fullName = p.getFullName(); // Copy the fullName from p
        this.explanation = explanation; // Initialize explanation
        this.vehicleOfPunishedPerson = vehicleOfPunishedPerson; // Initialize vehicleOfPunishedPerson
    }
    
    public void setExplanation(String explanation)
    {
    	this.explanation = explanation;
    }
    
    public void setVehicleOfPunishedPerson(Vehicle<?> vehicle)
    {
    	this.vehicleOfPunishedPerson = vehicle;
    }
    

}

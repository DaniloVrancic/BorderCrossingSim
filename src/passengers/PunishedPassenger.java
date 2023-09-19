package passengers;

import java.io.Serializable;

import custom_interfaces.Punishable;
import vehicles.Vehicle;

public class PunishedPassenger extends Passenger implements Serializable, Punishable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2673891012650992190L;
	//////////////////////////////////////
	String reasonOfPunishment;
	Vehicle<?> vehicleOfPunishedPerson;
	//////////////////////////////////////

	
    public PunishedPassenger(Passenger p) {
    	super();
        this.fullName = p.getFullName(); // Copy the fullName from p
        this.document = p.document;
        this.reasonOfPunishment = ""; // Initialize explanation (you can set this later if needed)
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
    public PunishedPassenger(Passenger p, String reasonOfPunishment, Vehicle<?> vehicleOfPunishedPerson) {
    	super();
    	this.document = p.document;	// Copy the reference from the punished Passenger
        this.fullName = p.getFullName(); // Copy the fullName from p
        this.reasonOfPunishment = reasonOfPunishment; // Initialize explanation
        this.vehicleOfPunishedPerson = vehicleOfPunishedPerson; // Initialize vehicleOfPunishedPerson
    }
    
    @Override
    public void setReasonOfPunishment(String reasonOfPunishment)
    {
    	this.reasonOfPunishment = reasonOfPunishment;
    }
    
    @Override 
    public String getReasonOfPunishment()
    {
    	return this.reasonOfPunishment;
    }
    
    public void setVehicleOfPunishedPerson(Vehicle<?> vehicle)
    {
    	this.vehicleOfPunishedPerson = vehicle;
    }
    
    @Override
    public String toString()
    {
    	String returnString = super.document.getFullName() + " (Passport Number: " + super.document.getPassportNumber() + ")";
    	
    	return returnString;
    }
    

}

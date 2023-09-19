package vehicles;

import java.io.Serializable;
import java.util.Objects;

import custom_interfaces.Punishable;


public class PunishedVehicle implements Serializable, Punishable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1683850844134517464L;
	
	
	String reasonOfPunishment;
	Vehicle<?> vehicle;
	////////////////////////////////////// CONSTRUCTOR
	public PunishedVehicle(Vehicle<?> vehicle, String reasonOfPunishment)
	{
		this.vehicle = vehicle;
		this.reasonOfPunishment = reasonOfPunishment;
	}
	
	///////////////////////////////////// GETTERS AND SETTERS
	
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

	public Vehicle<?> getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle<?> vehicle) {
		this.vehicle = vehicle;
	}

	
	@Override
	public boolean equals(Object obj) {   //Two PunishedVehicle objects will be equal only if the vehicle that they are referencing are the same
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;

	    PunishedVehicle other = (PunishedVehicle) obj;
	    return vehicle.equals((Vehicle<?>)other.vehicle);
	}

	@Override
	public int hashCode() { //The hashCode needs to correspond to the vehicle hashCode
	    return Objects.hashCode((Vehicle<?>)vehicle);
	}
	
	@Override
	public String toString()
	{
		String vehicleInfo = null;
		if(this.vehicle != null)
		{
			vehicleInfo = this.vehicle.getClass().getSimpleName() + " ID: " + this.vehicle.getVehicleId();
		}
		return vehicleInfo;
	}

	
	
	
}

package passengers;

import java.util.logging.Logger;

import logger.LoggerManager;
import util.random.IdentificationGenerator;

public class BusPassenger extends Passenger{
	
    private static Logger infoLogger = LoggerManager.getInfoLogger();
    private static Logger errorLogger = LoggerManager.getErrorLogger();

	private boolean hasLuggage = false;
	private boolean hasIllegalLuggage = false;
	
	private static final double HAVE_LUGGAGE_CHANGE = 0.7;
	private static final double HAVE_ILLEGAL_LUGGAGE_CHANCE = 0.1;
	
	/**
	 * The passenger will be created with luggage and illegal luggage already
	 * calculated properly and set. The checks for illegal luggage and 
	 * luggage generally can only be read, as there is no point in setting values that have been
	 * generated.
	 * @param generator the generator object that ensures the proper generation of Passenger details
	 */
	public BusPassenger(IdentificationGenerator generator) {
		super(generator);
		try
		{
			
		
		randomizeLuggageAndLegality();
		infoLogger.info("Bus passenger created: " + document.getFullName() + " has luggage: ( " + this.hasLuggage + " ), has illegal luggage: ( " + this.hasIllegalLuggage + " )\n");
		}
		catch(Exception ex)
		{
			errorLogger.severe("<Error creating bus passenger> " + ex.getMessage());
		}
	}
	
	public BusPassenger(String wantedName)
	{
		super(wantedName);
		try
		{
			
		
		randomizeLuggageAndLegality();
		infoLogger.info("Bus passenger created: " + document.getFullName() + " has luggage: ( " + this.hasLuggage + " ), has illegal luggage: ( " + this.hasIllegalLuggage + " )\n");
		}
		catch(Exception ex)
		{
			errorLogger.severe("<Error creating bus passenger> " + ex.getMessage());
		}
		
	}

	private void randomizeLuggageAndLegality() {
		
		if(Math.random() <= HAVE_LUGGAGE_CHANGE) //Calculate if the Passenger has Luggage or not
		{
			this.hasLuggage = true;
		}
		else
		{
			this.hasLuggage = false;
		}
		
		
		if(this.hasLuggage == true) //If the Passenger has luggage, check if his luggage is legal
		{
			if(Math.random() <= HAVE_ILLEGAL_LUGGAGE_CHANCE) // (per requirement, 10% chance for the luggage to be illegal)
			{
				this.hasIllegalLuggage = true;
			}
			else
			{
				this.hasIllegalLuggage = false;
			}
		}
		else //If the passenger doesn't have luggage, then his luggage can't be illegal anyways
		{
			this.hasIllegalLuggage = false;
		}
	}
	

	public boolean hasLuggage() {
		return hasLuggage;
	}
	
//	public void setLuggage(boolean hasLuggage) {
//		this.hasLuggage = hasLuggage;
//	}
	
	public boolean hasIllegalLuggage() {
		return hasIllegalLuggage;
	}
	
//	public void setIllegalLuggage(boolean hasIllegalLuggage) {
//		this.hasIllegalLuggage = hasIllegalLuggage;
//	}
}

package passengers;

import java.util.logging.Logger;

import logger.LoggerManager;
import util.random.IdentificationGenerator;

public class Passenger{
	//////////////////////////////////////////////////////////////////////
	private static Logger infoLogger = LoggerManager.getInfoLogger();	
	private static Logger errorLogger = LoggerManager.getErrorLogger();
	/////////////////////////////////////////////////////////////////////
	
	protected String fullName;
	public Identification document;
	
	/**
	 * Is going to generate a Passenger with his Identification randomized. The fullName from Identification
	 * is then used to be assigned to the fullName field in the Passenger class.
	 * @param generator passed through as a mechanism to ensure randomized data from a pool of predetermined values.
	 * The predetermined values are stored in a static way in the IdentificationDataGenerator class.
	 */
	public Passenger(IdentificationGenerator generator) //Dependency injection to ensure randomized attributes
	{
	    
		
		 try {
	            
			 Identification generatedInfo = generator.generateIdentification();
			 document = generatedInfo;
			 fullName = document.getFullName();

	            infoLogger.info("Passenger created: " + fullName);
	        } catch (Exception e) {
	            errorLogger.severe("<Error creating passenger> " + e.getMessage());
	        }
	}
	
	public Passenger(String fullName) //TEST THIS METHOD!
	{
		
		try {
			IdentificationGenerator generator = new IdentificationGenerator();
			
			this.fullName = fullName;
			document = generator.generateIdentification();
			document.setFullName(fullName);  //maybe fix this method!
            infoLogger.info("Passenger created: " + fullName);
        } catch (Exception ex) {
            errorLogger.severe("<Error creating passenger> " + ex.getMessage());
        }
		
	}
	
	/**
	 * Creates an empty Passenger whose data can later be filled.
	 */
	public Passenger()
	{
		this.document = null;
		this.fullName = "";
	}
	
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Override 
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("PUTNIK: " + this.fullName + "\n");
		sb.append(document);
		
		return sb.toString();
	}
}

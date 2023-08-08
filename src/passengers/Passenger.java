package passengers;

import util.random.IdentificationGenerator;

public class Passenger extends Thread{

	private String fullName;
	public Identification document;
	
	/**
	 * Is going to generate a Passenger with his Identification randomized. The fullName from Identification
	 * is then used to be assigned to the fullName field in the Passenger class.
	 * @param generator passed through as a mechanism to ensure randomized data from a pool of predetermined values.
	 * The predetermined values are stored in a static way in the IdentificationDataGenerator class.
	 */
	public Passenger(IdentificationGenerator generator) //Dependency injection to ensure randomized attributes
	{
		Identification generatedInfo = generator.generateIdentification();
		document = generatedInfo;
		fullName = document.getFullName();
	}
	
	public Passenger(String fullName) //TEST THIS METHOD!
	{
		IdentificationGenerator generator = new IdentificationGenerator();
		
		this.fullName = fullName;
		document = generator.generateIdentification();
		document.setFullName(fullName);  //maybe fix this method!
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

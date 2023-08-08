package passengers;

import util.random.IdentificationGenerator;

public class Passenger extends Thread{

	private String fullName;
	public Identification document;
	
	public Passenger(IdentificationGenerator generator)
	{
		Identification generatedInfo = generator.generateIdentification();
		document = new Identification();
		document.setFullName(generatedInfo.getFullName());
		document.setGender(generatedInfo.getGender());
		document.setPassportNumber(generatedInfo.getPassportNumber());
		document.setNationality(generatedInfo.getNationality());
		fullName = document.getFullName();
	}
	
	public Passenger(String fullName) //TEST THIS METHOD!
	{
		IdentificationGenerator generator = new IdentificationGenerator();
		
		document = generator.generateIdentification();
		this.fullName = fullName;
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

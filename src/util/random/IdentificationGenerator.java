package util.random;

import passengers.Identification;

public final class IdentificationGenerator {

	private Identification generated;
	
	public IdentificationGenerator()
	{
		generated = new Identification();
	}
	
	public Identification generateIdentification()
	{
		generated.setGender(IdentificationDataGenerator.generateGender());
		generated.setFullName(IdentificationDataGenerator.generateFullName(generated.getGender()));
		generated.setPassportNumber(IdentificationDataGenerator.generatePassportNumber());
		generated.setNationality(IdentificationDataGenerator.generateNationality());
		
		return generated;
	}
	
}

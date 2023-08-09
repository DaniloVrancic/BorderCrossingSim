package util.random;

import java.util.logging.Logger;

import logger.LoggerManager;
import passengers.Identification;

public final class IdentificationGenerator {

	private static Logger errorLogger = LoggerManager.getErrorLogger();
	
	private Identification generated;
	
	public IdentificationGenerator()
	{
		generated = new Identification();
	}
	
	public Identification generateIdentification()
	{
		try
		{
			generated = new Identification();
			
			generated.setGender(IdentificationDataGenerator.generateGender());
			generated.setFullName(IdentificationDataGenerator.generateFullName(generated.getGender()));
			generated.setPassportNumber(IdentificationDataGenerator.generatePassportNumber());
			generated.setNationality(IdentificationDataGenerator.generateNationality());			
		}
		catch(Exception ex)
		{
			errorLogger.severe("<Error generating identification>: " + ex.getMessage());
		}
		return generated;
	}
	
}

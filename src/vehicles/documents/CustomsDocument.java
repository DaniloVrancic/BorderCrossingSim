package vehicles.documents;

import java.util.logging.Logger;

import logger.LoggerManager;
import util.random.RandomGenerator;

public final class CustomsDocument {
	private static final Logger infoLogger = LoggerManager.getInfoLogger();

	private static double CHANCE_FOR_WEIGHT_OVER_LIMIT = 0.2;
	private static double WEIGHT_INCREASE_PERCENT = 0.3;
	
	
	private int declaredWeight;
	private int actualWeight;
	
	public CustomsDocument()
	{
		
		declaredWeight = RandomGenerator.generateInRange(2500, 7000);
		
		if(Math.random() <= CHANCE_FOR_WEIGHT_OVER_LIMIT)
		{
			this.actualWeight = (int)((1 + WEIGHT_INCREASE_PERCENT) * (double)declaredWeight);
		}
		else
		{
			this.actualWeight = this.declaredWeight;
		}
		
		infoLogger.info("<Customs document created successfully ><ACTUAL_WEIGHT = " + this.actualWeight + "><DECLARED_WEIGHT = " + this.declaredWeight + ">");
	}
	
	public int getActualWeight()
	{
		return this.actualWeight;
	}
	
	public int getDeclaredWeight()
	{
		return this.declaredWeight;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<CUSTOMS_DOCUMENT>\nDeclared weight: ");
		sb.append(this.declaredWeight + "\n");
		sb.append("Actual weight: ");
		sb.append(this.actualWeight + "\n");
		sb.append("</CUSTOMS_DOCUMENT>");
		return sb.toString();
	}
	
	
	
}

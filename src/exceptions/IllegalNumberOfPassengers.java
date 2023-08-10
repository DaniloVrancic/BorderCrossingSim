package exceptions;

public class IllegalNumberOfPassengers extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8254378430778736105L;
	private static String DEFAULT_MESSAGE = "Vehicle capacity is outside legal bounds!";
	
	public IllegalNumberOfPassengers()
	{
		super(DEFAULT_MESSAGE);
	}
	
	public IllegalNumberOfPassengers(String msg)
	{
		super(msg);
	}
	
	
}

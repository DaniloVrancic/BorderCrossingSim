package util.random;

import java.util.Random;

public class RandomGenerator {
	
	static Random rand;
	static
	{
		init();
	}
	
	public static void init()
	{
		rand = new Random();
	}
	
	public static int generateInRange(int minimum, int maximum)
	{
		if(minimum > maximum)
		{
			throw new IllegalArgumentException("<Illegal Argument> Minimum: " + minimum + " can't be higher than Maximum: " + maximum);
		}
		
		return rand.nextInt(maximum - minimum + 1) + minimum;
	}
}

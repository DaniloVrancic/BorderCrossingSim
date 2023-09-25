package terminals.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import terminals.CustomsTerminal;
import terminals.CustomsTerminalForOthers;
import terminals.CustomsTerminalForTrucks;

public class CustomsTerminalsManager {

	private static final int NUMBER_OF_CUSTOMS_TERMINALS_FOR_OTHERS = 1;
	private static final int NUMBER_OF_CUSTOMS_TERMINALS_FOR_TRUCKS = 1;

	public static List<CustomsTerminal> availableCustomsTerminals = new ArrayList<>();
	public static List<CustomsTerminal> availableCustomsTerminalsForOthers = new ArrayList<>();
	public static List<CustomsTerminal> availableCustomsTerminalsForTrucks = new ArrayList<>();
	
	static
	{
		init(NUMBER_OF_CUSTOMS_TERMINALS_FOR_OTHERS, NUMBER_OF_CUSTOMS_TERMINALS_FOR_TRUCKS);
	}
	/**
	 * Initializes the list of Terminals
	 * @param numberOfOthersTerminals The number of terminals that can process Bus and Automobile
	 * @param numberOfTruckTerminals The number of terminals that can process Truck
	 */
	private CustomsTerminalsManager()
	{
	}
	
	private static void init(int numberOfOthersTerminals, int numberOfTruckTerminals)
	{
		CustomsTerminalForOthers[] customsTerminalsForOthersArray = new CustomsTerminalForOthers[numberOfOthersTerminals];
		CustomsTerminalForTrucks[] customsTerminalsForTrucksArray = new CustomsTerminalForTrucks[numberOfTruckTerminals];
		
		for(int i = 0; i < customsTerminalsForOthersArray.length; i++)
		{
			customsTerminalsForOthersArray[i] = new CustomsTerminalForOthers();
		}
		for(int i = 0; i < customsTerminalsForTrucksArray.length; i++)
		{
			customsTerminalsForTrucksArray[i] = new CustomsTerminalForTrucks();
		}
		
		availableCustomsTerminalsForOthers = Arrays.asList(customsTerminalsForOthersArray);
		availableCustomsTerminalsForTrucks = Arrays.asList(customsTerminalsForTrucksArray);
		availableCustomsTerminals.addAll(availableCustomsTerminalsForOthers);
		availableCustomsTerminals.addAll(availableCustomsTerminalsForTrucks);
	}
}

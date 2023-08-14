package terminals.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import application.Main;
import terminals.PoliceTerminal;
import terminals.PoliceTerminalForOthers;
import terminals.PoliceTerminalForTrucks;

public class PoliceTerminalsManager {
	
	private static final int NUMBER_OF_POLICE_TERMINALS_FOR_OTHERS = 2;
	private static final int NUMBER_OF_POLICE_TERMINALS_FOR_TRUCKS = 1;

	public static List<PoliceTerminal> availablePoliceTerminals = new ArrayList<>();
	public static List<PoliceTerminal> availablePoliceTerminalsForOthers = new ArrayList<>();
	public static List<PoliceTerminal> availablePoliceTerminalsForTrucks = new ArrayList<>();
	
	static
	{
		init(NUMBER_OF_POLICE_TERMINALS_FOR_OTHERS, NUMBER_OF_POLICE_TERMINALS_FOR_TRUCKS);
	}
	/**
	 * Initializes the list of Terminals
	 * @param numberOfOthersTerminals The number of terminals that can process Bus and Automobile
	 * @param numberOfTruckTerminals The number of terminals that can process Truck
	 */
	private PoliceTerminalsManager()
	{
	}
	
	private static void init(int numberOfOthersTerminals, int numberOfTruckTerminals)
	{
		PoliceTerminalForOthers[] policeTerminalsForOthersArray = new PoliceTerminalForOthers[numberOfOthersTerminals];
		PoliceTerminalForTrucks[] policeTerminalsForTrucksArray = new PoliceTerminalForTrucks[numberOfTruckTerminals];
		
		for(int i = 0; i < policeTerminalsForOthersArray.length; i++)
		{
			policeTerminalsForOthersArray[i] = new PoliceTerminalForOthers(Main.vehicleQueue);
		}
		for(int i = 0; i < policeTerminalsForTrucksArray.length; i++)
		{
			policeTerminalsForTrucksArray[i] = new PoliceTerminalForTrucks(Main.vehicleQueue);
		}
		
		availablePoliceTerminalsForOthers = Arrays.asList(policeTerminalsForOthersArray);
		availablePoliceTerminalsForTrucks = Arrays.asList(policeTerminalsForTrucksArray);
		availablePoliceTerminals.addAll(availablePoliceTerminalsForOthers);
		availablePoliceTerminals.addAll(availablePoliceTerminalsForTrucks);
	}
}

package passengers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vehicles.Vehicle;
import vehicles.seriazilabledatastructure.SerializableList;

/**
 * To be used for listing all the Vehicles that contained punished people inside the specified vehicle, as well as listing all vehicles that had an incident
 */
public class PunishedPersonManager {

	private static String PATH_TO_PUNISHED_PERSON_LIST_SERIALIZED = getTerminalStatusFileName();
    public static Map<Vehicle<?>, SerializableList<PunishedPassenger>> allPunishmentsMap = new HashMap<>(); //Vehicle of the punished person serves as the key to listing all the punished people of the vehicle

    public static void addPunishment(PunishedPassenger p) {
        Vehicle<?> vehicle = p.vehicleOfPunishedPerson;

        // Check if the corresponding map exists for the vehicle
        synchronized (allPunishmentsMap) 
        {
			
        if (!allPunishmentsMap.containsKey(vehicle)) 
        	{
        	allPunishmentsMap.put(vehicle, new SerializableList<>(PATH_TO_PUNISHED_PERSON_LIST_SERIALIZED)); //If it doesn't exist, create it
        	}

        // Add the punished passenger to the list
        SerializableList<PunishedPassenger> punishedPassengers = allPunishmentsMap.get(vehicle);
        punishedPassengers.add(p); //Add the passenger to the corresponding list
        }
    }
    
    public static String getPunishedPersonListFilePath()
    {
    	return PATH_TO_PUNISHED_PERSON_LIST_SERIALIZED;
    }
    
    private static String getTerminalStatusFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String formattedDate = dateFormat.format(new Date());
        return "serialization/passengers/" + formattedDate + "_PunishedPassengers.ser";
    }
}

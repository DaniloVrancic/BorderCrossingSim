package passengers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vehicles.Vehicle;
import vehicles.seriazilabledatastructure.SerializableList;

public class PunishedPersonManager {

	private static String PATH_TO_PUNISHED_PERSON_LIST_SERIALIZED = getTerminalStatusFileName();
    public static Map<Vehicle<?>, SerializableList<PunishedPassenger>> allPunishments = new HashMap<>();

    public static void addPunishment(PunishedPassenger p) {
        Vehicle<?> vehicle = p.vehicleOfPunishedPerson;

        // Check if the corresponding map exists for the vehicle
        synchronized (allPunishments) 
        {
			
        if (!allPunishments.containsKey(vehicle)) 
        	{
            allPunishments.put(vehicle, new SerializableList<>(PATH_TO_PUNISHED_PERSON_LIST_SERIALIZED)); //If it doesn't exist, create it
        	}

        // Add the punished passenger to the list
        List<PunishedPassenger> punishedPassengers = allPunishments.get(vehicle);
        punishedPassengers.add(p); //Add the passenger to the corresponding list
        }
    }
    
    private static String getTerminalStatusFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String formattedDate = dateFormat.format(new Date());
        return "serialization/passengers/" + formattedDate + "_PunishedPassengers.ser";
    }
}

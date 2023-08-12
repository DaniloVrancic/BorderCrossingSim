package passengers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vehicles.Vehicle;

public class PunishedPersonManager {

    public static Map<Vehicle<?>, List<PunishedPassenger>> allPunishments = new HashMap<>();

    public static void addPunishment(PunishedPassenger p) {
        Vehicle<?> vehicle = p.vehicleOfPunishedPerson;

        // Check if the corresponding map exists for the vehicle
        synchronized (allPunishments) 
        {
			
        if (!allPunishments.containsKey(vehicle)) 
        	{
            allPunishments.put(vehicle, new ArrayList<>()); //If it doesn't exist, create it
        	}

        // Add the punished passenger to the list
        List<PunishedPassenger> punishedPassengers = allPunishments.get(vehicle);
        punishedPassengers.add(p); //Add the passenger to the corresponding list
        }
    }
}

package passengers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import vehicles.PunishedVehicle;
import vehicles.seriazilabledatastructure.SerializableHashMap;

/**
 * To be used for listing all the Vehicles that contained punished people inside the specified vehicle, as well as listing all vehicles that had an incident
 */
public class PunishmentManager {

	private static String PATH_TO_LIST_OF_PUNISHED_SERIALIZED = getTerminalStatusFileName();
    public static Map<PunishedVehicle, List<PunishedPassenger>> allPunishmentsMap = new SerializableHashMap(PATH_TO_LIST_OF_PUNISHED_SERIALIZED); //Vehicle of the punished person serves as the key to listing all the punished people of the vehicle

    public static void addPunishment(PunishedPassenger p) {
        PunishedVehicle p_vehicle = new PunishedVehicle(p.vehicleOfPunishedPerson,"");
        
        
        // Check if the corresponding map exists for the vehicle
        synchronized (allPunishmentsMap) 
        {
			
        if (!allPunishmentsMap.containsKey(p_vehicle)) 
        	{
        	List<PunishedPassenger> newList = new LinkedList<>(); //associate a new List with every vehicle punished
        	
        		newList.add(p);
        		allPunishmentsMap.put(p_vehicle, newList); //If it doesn't exist, create it        		
        	
        	}
        else //if the vehicle has already been registered earlier
        	{
        	
        		List<PunishedPassenger> retrievedPassengerList = allPunishmentsMap.get(p_vehicle); //retrieves the list associated with the existing vehicle
        			
        			if(retrievedPassengerList != null)
        				retrievedPassengerList.add(p);        							
				
        		
        	}
        }
    } //end of addPunishment method
    
    public static void addPunishment(PunishedVehicle p_vehicle) {

        // Check if the corresponding map exists for the vehicle
        synchronized (allPunishmentsMap) 
        {
			
        if (!allPunishmentsMap.containsKey(p_vehicle)) 
        	{
        	allPunishmentsMap.put(p_vehicle, new LinkedList<>()); //If it doesn't exist, create it
        	}
        else //else if that vehicle exists in the keySet
        	{ //this part will only copy the reason of throwing the vehicle out
        	for(PunishedVehicle pv : allPunishmentsMap.keySet())
        		{
        		if(pv.equals(p_vehicle))
        			{
        			pv.setReasonOfPunishment(p_vehicle.getReasonOfPunishment());
        			break;
        			}
        		}
        	}
        } //end of synchronized block
    } //end of addPunishment method
    
    public static String getPunishedPersonListFilePath()
    {
    	return PATH_TO_LIST_OF_PUNISHED_SERIALIZED;
    }
    
    private static String getTerminalStatusFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String formattedDate = dateFormat.format(new Date());
        return "serialization/passengers/" + formattedDate + "_PunishedPassengers.ser";
    }
}

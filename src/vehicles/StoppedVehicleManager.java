package vehicles;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import logger.LoggerManager;
import vehicles.writetofilehashmap.WriteToFileHashMap;

public class StoppedVehicleManager {
	private static ReentrantLock lock = new ReentrantLock();
	private static Logger errorLogger = LoggerManager.getErrorLogger();
	private static String PATH_TO_STOPPED_VEHICLES_TEXT_FILE = getTerminalStatusFileName();

    public static Map<Vehicle<?>, String> stoppedVehicles = new WriteToFileHashMap<>(getTerminalStatusFileName()); //To be used as a mechanism to write to files, at the same time when adding elements to the hashMap

    public static void addStoppedVehicle(Vehicle<?> vehicle, String reason) {
    	lock.lock();
    	try {
    		stoppedVehicles.put(vehicle, reason);
		} catch (Exception e) {
			errorLogger.severe("<VEHICLE PUTTING IN MAP EXCEPTION>: " + e.getMessage());
		}
    	finally
    	{
    		lock.unlock();
    	}
    }
    
    public static String getStoppedVehiclesPath()
    {
    	return PATH_TO_STOPPED_VEHICLES_TEXT_FILE;
    }

    
    private static String getTerminalStatusFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String formattedDate = dateFormat.format(new Date());
        return "vehicleUpdates/" + formattedDate + "_StoppedVehicles.txt";
    }
}

package vehicles;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import logger.LoggerManager;

public class StoppedVehicleManager {
	private static ReentrantLock lock = new ReentrantLock();
	private static Logger errorLogger = LoggerManager.getErrorLogger();

    private static Map<Vehicle<?>, String> stoppedVehicles = new HashMap<>();

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

    public static Map<Vehicle<?>, String> getStoppedVehicles() {
        lock.lock();
        try {
            return new HashMap<>(stoppedVehicles);
        } finally {
            lock.unlock();
        }
    }
}

package vehicles.writetofilehashmap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import logger.LoggerManager;

/**
 * Overrides the original functionality of put, adding 
 * a method that at the same time also adds details about
 * the Map input into a text file.
 * @param <K> Key type
 * @param <V> Value type
 */
public class WriteToFileHashMap<K, V> extends HashMap<K, V> {

	Logger errorLogger = LoggerManager.getErrorLogger();
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6110775936192940264L;
	
	
	private String fileName;

    public WriteToFileHashMap(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public V put(K key, V value) {
        V oldValue = super.put(key, value);
        writeToFile();
        return oldValue;
    }

    private void writeToFile() {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            for (Map.Entry<K, V> entry : entrySet()) {
                writer.write("Vehicle:" + entry.getKey() + " STOPPED FOR REASON:  " + entry.getValue() + System.lineSeparator());
            }
            clear();
        } catch (IOException e) {
        	errorLogger.severe("<IOException during writing HashMap to File>: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}

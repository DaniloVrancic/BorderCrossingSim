package vehicles.seriazilabledatastructure;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Logger;

import logger.LoggerManager;

public class SerializableHashMap<K, V> extends HashMap<K, V> implements Serializable {

    private static final long serialVersionUID = 123456789L;

    private static Logger errorLogger = LoggerManager.getErrorLogger();

    private String filePath;
    

    public SerializableHashMap(String filePath) {
        this.filePath = filePath;
        deserialize(); // Load existing data, if any
    }

    public String getFilePath() {
        return this.filePath;
    }

    @SuppressWarnings("unchecked")
    private void deserialize() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            HashMap<K, V> loadedMap = (HashMap<K, V>) ois.readObject();
            this.putAll(loadedMap);
        } catch (IOException | ClassNotFoundException ex) {
            // Handle the case where no data exists yet
            errorLogger.severe("<Deserialization failure>: " + ex.getMessage());
        }
    }

    private void serializeMap() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
        } catch (IOException ex) {
            errorLogger.severe("<Serialization failure>: " + ex.getMessage());
        }
    }
    
    @Override
    public V put(K key, V value) {
    	
    		V result = super.put(key, value);
    		serializeMap();			
    		return result;
		
    }

}

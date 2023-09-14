package vehicles.seriazilabledatastructure;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

import logger.LoggerManager;

public class SerializableList<T> extends ArrayList<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5462513798889879578L;

	
	private static Logger errorLogger = LoggerManager.getErrorLogger();
	
	private static String filePath;
	
	public SerializableList(String filePath)
	{
		SerializableList.filePath = filePath;
	}
	
	public String getFilePath()
	{
		return this.filePath;
	}
	
	@Override
	public boolean add(T element)
	{
		boolean result = super.add(element);
		if(result == true)
		serializeList();
		return result;
	}
	
	private void serializeList()
	{
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath, true)))
		{
			oos.writeObject(this);
		}
		catch(IOException ex)
		{
			errorLogger.severe("<Serialization failure>: " + ex.getMessage());
		}
	}
	
	public static SerializableList<?> deserialize(String filePath)
	{
		SerializableList<?> list = null;
		
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath)))
		{
			list = (SerializableList<?>) ois.readObject();
		}
		catch(IOException | ClassNotFoundException ex)
		{
			errorLogger.severe("<Deserialization failure>: " + ex.getMessage());
		}
		return list;
	}
	
	public static SerializableList<?> deserialize()
	{
		if(SerializableList.filePath != null)
		{
			return deserialize(SerializableList.filePath);
		}
		else
		{
			return null;
		}
	}
	
	
}

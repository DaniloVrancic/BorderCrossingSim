package vehicles.seriazilabledatastructure;

import java.io.FileOutputStream;
import java.io.IOException;
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
	
	private String filePath;
	public SerializableList(String filePath)
	{
		this.filePath = filePath;
	}
	
	@Override
	public boolean add(T element)
	{
		boolean result = super.add(element);
		serializeList();
		return result;
	}
	
	private void serializeList()
	{
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath)))
		{
			oos.writeObject(this);
		}
		catch(IOException ex)
		{
			errorLogger.severe("<Serialization failure>: " + ex.getMessage());
		}
	}
}

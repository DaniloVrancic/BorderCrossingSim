package terminals.managers;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import gui.BorderCrossingGUIController;
import logger.LoggerManager;
import terminals.CustomsTerminal;
import terminals.PoliceTerminal;
import terminals.PoliceTerminalForOthers;
import terminals.PoliceTerminalForTrucks;
import terminals.Terminal;
import vehicles.Vehicle;

public class TerminalBlocker implements Runnable
{
	List<PoliceTerminal> policeTerminals = PoliceTerminalsManager.availablePoliceTerminals;
	List<CustomsTerminal> customsTerminals = CustomsTerminalsManager.availableCustomsTerminals;
	BlockingQueue<Vehicle<?>> vehicleQueue = BorderCrossingGUIController.vehicleQueue;
	
	//List<PoliceTerminal> policeTerminalsForOthers_ToNotify = PoliceTerminalsManager.availablePoliceTerminalsForOthers;
	List<PoliceTerminal> availablePoliceTerminals = PoliceTerminalsManager.availablePoliceTerminalsForTrucks;

	public void run()
		{
		
		String fileDir = "./terminalsController/";
		Path pathToFiles = Paths.get(fileDir); //Path where I want the Watcher to monitor
		
		try
		{
			
		
		WatchService watcher = FileSystems.getDefault().newWatchService();
		
		pathToFiles.register(watcher, ENTRY_MODIFY);
		//System.out.println("WatchService registered for: " + pathToFiles.toString());
		
		while(true)
		{
			WatchKey takenKey = null;
			try {
				takenKey = watcher.take();
			} catch (InterruptedException e) {
				Logger errorLogger = LoggerManager.getErrorLogger();
				errorLogger.severe("<INTERRUPTED WATCHER TAKE>:" + e.getLocalizedMessage());
				System.out.println(e.getLocalizedMessage());
			}
			
			for(WatchEvent<?> event : takenKey.pollEvents())
			{
				WatchEvent.Kind<?> kind = event.kind();
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path pathOfChange = (Path) ev.context();
				parseFile(pathToFiles.resolve(pathOfChange).toString());
				
			}
			takenKey.reset();
			
			boolean valid = takenKey.reset();
			if(!valid)
			{
				break;
			}
		}
		}
		catch(IOException ex)
		{
			System.err.println(ex.getLocalizedMessage());
		}
	}
	
	private void parseFile(String filePath)
	{
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by '='
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    if(!key.toUpperCase().equals("BLOCKED"))
                    {
                    	continue;
                    }
                    String value = parts[1].trim();
                    value = value.toUpperCase();
                    
                    String[] tokens = value.split(",");
                    Terminal selectedTerminal;
                    if("P".equals(tokens[0].trim()) && tokens.length == policeTerminals.size() + 1) // +1 because of first attribute
                    {
                    	synchronized (policeTerminals) 
                    	{
                    		for(int i = 1; i < tokens.length; ++i)
                    		{
                    			selectedTerminal = policeTerminals.get(i - 1);
                    			if("1".equals(tokens[i]))
                    			{
                    				//BLOCK POLICE TERMINAL
                    				synchronized (selectedTerminal) {
                    					selectedTerminal.setBlocked(true);										
									}
                    				System.out.println("BLOCKED POLICE TERMINAL : " + selectedTerminal);
                    			}
                    			else if("0".equals(tokens[i]))
                    			{
                    				//UNBLOCK POLICE TERMINAL
                    				synchronized (selectedTerminal) {
                    					//selectedTerminal.unblockTerminal();
                    					selectedTerminal.setBlocked(false);
                    					if(selectedTerminal instanceof PoliceTerminalForOthers)
                    					{
                    						//synchronized (policeTerminalsForOthers_ToNotify) {
                    						//	policeTerminalsForOthers_ToNotify.notifyAll();
                    						//}
                    						
                    					}
                    					else if(selectedTerminal instanceof PoliceTerminalForTrucks)
                    					{
                    						synchronized (availablePoliceTerminals) {
                    							availablePoliceTerminals.notifyAll();
                    							System.out.println("NOTIFIED ALL POLICETERMINALSFOR_TRUCKS");
                    						}
                    						
                    					}
									}
                    				System.out.println("UNBLOCKED POLICE TERMINAL : " + selectedTerminal);
                               	}
                    	
                    		}//end for
                    	}//end synchronized
                    	}
                    
                    else if("C".equals(tokens[0]) && tokens.length == customsTerminals.size() + 1)
                    {
                    	
                    		for(int i = 1; i < tokens.length; ++i)
                    		{
                    			selectedTerminal = customsTerminals.get(i - 1);
                    			if("1".equals(tokens[i]))
                    			{
                    				//BLOCK CUSTOMS TERMINAL
                    				System.out.println("BLOCKED CUSTOMS TERMINAL : " + selectedTerminal);
                    			}
                    			else if("0".equals(tokens[i]))
                    			{
                    				// UNBLOCK CUSTOMS TERMINAL
                    				System.out.println("UNBLOCKED CUSTOMS TERMINAL : " + selectedTerminal);
                    				}
                    			}
                    		}//end for
                    }
                    
                    // Process the key and value here
                    //System.out.println("Key: " + key + ", Value: " + value);
                }
            }
        catch (IOException e) {
            e.printStackTrace();
        }
		
	}//end of method
	
	List<Terminal> allTerminals = new ArrayList<Terminal>();
	private boolean allTerminalsEmpty() {
		if(allTerminals.size() < 1)
		{
			allTerminals.addAll(policeTerminals);
			allTerminals.addAll(customsTerminals);			
		}

	    for (Terminal terminal : allTerminals) {
	        if (terminal.getVehicleAtTerminal() != null) {
	            return false; // If at least one terminal has a vehicle, return false
	        }
	    }

	    return true; // If all terminals are empty, return true
	}
	
	private boolean isVehicleQueueEmpty(BlockingQueue<?> queue)
	{
		if(queue.size() > 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

}

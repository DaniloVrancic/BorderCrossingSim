package terminals.managers;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Logger;

import logger.LoggerManager;

public class TerminalBlocker implements Runnable
{
	

	public void run()
		{
		
		String fileDir = ".\terminalsController";
		Path pathToFiles = Paths.get(fileDir); //Path where I want the Watcher to monitor
		
		try
		{
			
		
		WatchService watcher = FileSystems.getDefault().newWatchService();
		
		pathToFiles.register(watcher, ENTRY_MODIFY);
		System.out.println("WatchService registered for: " + pathToFiles.toString());
		
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
				System.out.println(kind.name() + ": " + pathOfChange);
				parseFile(pathOfChange);
				
			}
			
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
	
	private void parseFile(String file)
	{
		
	} //COMPLETE THIS

	
}

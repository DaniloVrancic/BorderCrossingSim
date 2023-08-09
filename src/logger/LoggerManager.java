package logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerManager {
	
    private static final String LOGS_FOLDER = "logs/";
    private static final String INFO_LOG_FILE = "info.log";
    private static final String ERROR_LOG_FILE = "error.log";
    
    
    private static final Logger infoLogger = Logger.getLogger("InfoLogger");
    private static final Logger errorLogger = Logger.getLogger("ErrorLogger");

    private static LoggerManager instance = null;

    private LoggerManager() {
        configureLogger(infoLogger, LOGS_FOLDER + INFO_LOG_FILE, Level.INFO);
        configureLogger(errorLogger, LOGS_FOLDER + ERROR_LOG_FILE, Level.SEVERE);
    }

    public static Logger getInfoLogger() {
        if (instance == null) {
            instance = new LoggerManager();
        }
        return infoLogger;
    }

    public static Logger getErrorLogger() {
        if (instance == null) {
            instance = new LoggerManager();
        }
        return errorLogger;
    }

    private void configureLogger(Logger logger, String fileName, Level level) {
        logger.setLevel(level);
        try {
        	logger.setUseParentHandlers(false); //removes the console from being printed on by loggers
        	
            Handler fileHandler = new FileHandler(fileName);
            fileHandler.setLevel(level);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



	package terminals;
	
	import java.util.concurrent.locks.ReentrantLock;
	import java.util.logging.Logger;
	
	import logger.LoggerManager;
	import vehicles.Vehicle;
	
	public abstract class Terminal {
		private static int numberOfTerminals = 0;
		protected static Logger errorLogger = LoggerManager.getErrorLogger();
		protected static Logger infoLogger = LoggerManager.getInfoLogger();
		
	    protected TerminalStatus status;
	    protected Vehicle<?> vehicleAtTerminal;
	    protected int id;
	    protected  ReentrantLock lock = new ReentrantLock();
	    
	    
	
	    public Terminal() {
	        status = TerminalStatus.AVAILABLE;
	        vehicleAtTerminal = null;
	        this.id = Terminal.numberOfTerminals++;
	    }
	    
	    public TerminalStatus getStatus()
	    {
	    	return this.status;
	    }
	    
	    public Vehicle<?> getVehicleAtTerminal()
	    {
	    	return this.vehicleAtTerminal;
	    }
	    
	    public void setVehicleAtTerminal(Vehicle<?> vehicle)
	    {
	    	this.vehicleAtTerminal = vehicle;
	    }
	
	
	
	    public void blockTerminal()
	    {
	    	this.status = TerminalStatus.BLOCKED;
	    }
	    
	    public boolean isAvailable() {
	        return lock.tryLock(); // Returns true if the lock is available, false otherwise
	    }
	    
	    /**
	     * Releases the lock from being used up by this object.
	     */
	    public void release() {
	        lock.unlock();
	    }
	    
	    public void lock()
	    {
	    	lock.lock();
	    }
	}

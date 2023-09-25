		package terminals;
		
		import java.util.concurrent.locks.ReentrantLock;
		import java.util.logging.Logger;
	
	import gui.BorderCrossingGUIController;
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
		    public Object pauseObject = new Object(); //Every object has their own pauseObject
		    protected boolean isBlocked;
			protected boolean isPaused;
		    
		    
		
		    public Terminal() {
		        status = TerminalStatus.AVAILABLE;
		        isBlocked = false;
		        vehicleAtTerminal = null;
		        this.id = Terminal.numberOfTerminals++;
		    }
		    
		    public TerminalStatus getStatus()
		    {
		    	return this.status;
		    }
		    
		    public void setStatus(TerminalStatus status)
		    {
		    	this.status = status;
		    }
		    
		    public Vehicle<?> getVehicleAtTerminal()
		    {
		    	return this.vehicleAtTerminal;
		    }
		    
		    public void setVehicleAtTerminal(Vehicle<?> vehicle)
		    {
		    	this.vehicleAtTerminal = vehicle;
		    }
		
		    public void setVehicleAndRemoveFromQueue()
		    {
		    	setVehicleAtTerminal(BorderCrossingGUIController.vehicleQueue.poll()); //removes from queue and takes the reference
		    	if(!BorderCrossingGUIController.vehicleQueue.isEmpty())
		    	{
		    		BorderCrossingGUIController.vehicleQueue.peek().start();
		    	}
		    }
		
		
		    public void setBlocked(boolean blocked) {
		    	synchronized (this) {
		    		this.isBlocked = blocked;					
				}
		    }
		    
		    public boolean isBlocked()
		    {
		    	synchronized (this) {
		    		return this.isBlocked;					
				}
				
		    }
		    
		    public void unblockTerminal() {
		        synchronized (pauseObject) {
		            this.isBlocked = false;
		            pauseObject.notify();
		        }
		    }
	
		    public void waitWhileBlocked()
		    {
		    	try
		    	{
		    	synchronized (pauseObject){
		    		while(this.isBlocked == true)
		    		{
		    			System.out.println("THREAD: " + Thread.currentThread().threadId() + " HIT THE waitWhileBlocked() method BEFORE wait");
		    			pauseObject.wait();
		    			System.out.println("THREAD: " + Thread.currentThread().threadId() + " HIT THE waitWhileBlocked() method AFTER wait");
		    		}
					}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		    
		    
		    public boolean isAvailable() {
		        return lock.tryLock(); // Returns true if the lock is available, false otherwise
		    }
		    
		    /**
		     * Releases the lock from being used up by this object.
		     */
		    public void release() {
		    	synchronized (this) {
		    		if(lock.isLocked())
		    		{
		    			lock.unlock();
		    		}				
				}
		    }
		    
		    public void lock()
		    {
		    	lock.lock();
		    }
		    
		    
		    
		    @Override
		    public int hashCode() {
		        final int prime = 31;
		        int result = 1;
		        result = prime * result + id;
		        return result;
		    }
	
		    @Override
		    public boolean equals(Object obj) {
		        if (this == obj)
		            return true;
		        if (obj == null || getClass() != obj.getClass())
		            return false;
		        Terminal other = (Terminal) obj;
		        return id == other.id;
		    }
		}

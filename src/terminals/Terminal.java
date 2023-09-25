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
		    protected ReentrantLock blockLock = new ReentrantLock();
		    protected boolean isBlocked;
		    
		    
		
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
		        this.isBlocked = blocked;
		        if (isBlocked) {
		        	blockLock.tryLock();
		            try {
		                // Notify any waiting threads
		            	synchronized (blockLock) {						
		            		blockLock.notifyAll();
						}
		            	synchronized (this) {
							this.notifyAll();
						}
		            } finally {
		                //lock.unlock();
		            }
		        }
		        if(isBlocked == false)
		        {
		        	synchronized (blockLock) {
		        		if(blockLock.isLocked())
		        		{	        			
		        			blockLock.unlock();
		        		}
		        		blockLock.notifyAll();
		        	}
		        		synchronized (this) {
		        			this.notifyAll();						
						}
		        }
		    }
		    
		    public boolean isBlocked()
		    {
		    	
						return this.isBlocked;
				
		    }
		    
		    public void unblockTerminal() {
		        synchronized (this) {
		            this.isBlocked = false;
		           
		                this.notifyAll(); // Moved inside synchronized block
		            
		        }
		        synchronized (blockLock) {
		        	blockLock.notifyAll();
					release();
				}
		    }
	
	
		    
		    
		    public void waitWhileBlocked()
		    {
		    	try {
		    		synchronized (this) {
		    			while(this.isBlocked == true)
		    			{
		    				synchronized (blockLock) {
		    					System.out.println("VEHICLE: " + this.getVehicleAtTerminal().getVehicleId() + " IS WAITING AT TERMINAL FROZen");
		    					blockLock.wait();
		    					System.out.println("VEHICLE: " + this.getVehicleAtTerminal().getVehicleId() + " TRYING AGAIN");
		    				}
		    				
		    			}
						
					}
		    	}
					 catch (InterruptedException e) {
						// TODO Auto-generated catch block
						errorLogger.severe(e.getLocalizedMessage());
					 }
		    }
		    
		    public void exitWhileBlocked()
		    {
		    	synchronized (this) {
		    		this.isBlocked = false;
				}
		    	synchronized (blockLock) {
		    		blockLock.notifyAll();				
				}
		    }
		    
		    public boolean isAvailable() {
		        return lock.tryLock(); // Returns true if the lock is available, false otherwise
		    }
		    
		    /**
		     * Releases the lock from being used up by this object.
		     */
		    public void release() {
		    	synchronized (lock) {
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

package terminals;

import java.util.List;

import vehicles.Automobile;
import vehicles.Bus;
import vehicles.Truck;

public class CustomsTerminalForTrucks extends CustomsTerminal{

	public CustomsTerminalForTrucks(List<PoliceTerminal> terminalsToWatch)
	{
		super(terminalsToWatch);
	}
	
	private static final int TIME_PLACEHOLDER = 100;
	
	public void processVehicle()
	{
		try
		{
			if(this.vehicleAtTerminal instanceof Truck)
			{
				this.status = TerminalStatus.PROCESSING;
				super.processVehicle(TIME_PLACEHOLDER);
			}
			infoLogger.info("Finished with truck (id): " + this.vehicleAtTerminal.getVehicleId());
		}
		catch(InterruptedException ex)
		{
			errorLogger.severe("<PROCESSING VEHICLE AT CUSTOMS FOR TRUCKS MISTAKE>: " + ex.getMessage());
		}
		finally
		{
			this.status = TerminalStatus.AVAILABLE;
			this.vehicleAtTerminal = null;
			
			for(PoliceTerminal terminal : watchingTerminals)
			{
				if(terminal.vehicleAtTerminal != null)
				{
					synchronized (terminal.vehicleAtTerminal) 
					{
						terminal.vehicleAtTerminal.notify();
					}
				} //end of if
			}//end of for
		}// end of finally
	}//end of processVehicle (Method)
	
}

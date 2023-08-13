package terminals;

import java.util.List;

import vehicles.Automobile;
import vehicles.Bus;

public class CustomsTerminalForOthers extends CustomsTerminal{

	private static final int TIME_FOR_AUTOMOBILE_PROCESSING = 2000;
	private static final int TIME_FOR_BUS_PASSENGER_PROCESSING = 100;
	
	public CustomsTerminalForOthers(List<PoliceTerminal> terminalsToWatch)
	{
		super(terminalsToWatch);
	}
	
	public void processVehicle()
	{
		try
		{
			if(this.vehicleAtTerminal instanceof Bus)
			{
				this.status = TerminalStatus.PROCESSING;
				super.processVehicle(TIME_FOR_BUS_PASSENGER_PROCESSING);
			}
			if(this.vehicleAtTerminal instanceof Automobile)
			{
				this.status = TerminalStatus.PROCESSING;
				super.processVehicle(TIME_FOR_AUTOMOBILE_PROCESSING);
			}
			infoLogger.info("Finished with vehicle (id): " + this.vehicleAtTerminal.getVehicleId());
		}
		catch(InterruptedException ex)
		{
			errorLogger.severe("<PROCESSING VEHICLE AT CUSTOMS FOR OTHERS MISTAKE>: " + ex.getMessage());
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

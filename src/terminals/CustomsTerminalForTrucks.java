package terminals;

import java.util.List;

import vehicles.Truck;

public class CustomsTerminalForTrucks extends CustomsTerminal{

	public CustomsTerminalForTrucks(List<PoliceTerminal> terminalsToWatch)
	{
		super(terminalsToWatch);
	}
	
	public CustomsTerminalForTrucks()
	{
		super();
	}
	
	private static final int TIME_TO_WAIT_AT_CUSTOMS = 1000;
	
	public void processVehicle()
	{
		try
		{
			if(this.vehicleAtTerminal instanceof Truck)
			{
				this.status = TerminalStatus.PROCESSING;
				super.processVehicle(TIME_TO_WAIT_AT_CUSTOMS);
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
			
		}// end of finally
	}//end of processVehicle (Method)
	
}

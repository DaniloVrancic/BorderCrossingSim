package application;
	
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.application.Application;
import javafx.stage.Stage;
import passengers.BusPassenger;
import passengers.Passenger;
import terminals.CustomsTerminal;
import terminals.CustomsTerminalForOthers;
import terminals.CustomsTerminalForTrucks;
import terminals.PoliceTerminal;
import terminals.PoliceTerminalForOthers;
import terminals.PoliceTerminalForTrucks;
import util.random.IdentificationGenerator;
import util.random.RandomGenerator;
import vehicles.Vehicle;
import vehicles.Bus;
import vehicles.Automobile;
import vehicles.Truck;
import vehicles.documents.CustomsDocument;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("BorderCrossingGUI.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Our Stage");
			//primaryStage.show();
			
			final int NUMBER_OF_BUSES_AT_START 		= 5;
			final int NUMBER_OF_TRUCKS_AT_START 	= 10;
			final int NUMBER_OF_CARS_AT_START 		= 35;
			
			IdentificationGenerator generator = new IdentificationGenerator();
			List<Vehicle<?>> listToShuffle = fillAndShuffleList(NUMBER_OF_BUSES_AT_START, NUMBER_OF_TRUCKS_AT_START,
					NUMBER_OF_CARS_AT_START);
			
			BlockingQueue<Vehicle<?>> vehicleQueue = new LinkedBlockingQueue<>(listToShuffle);
			
//			for(Vehicle<?> vehicle : vehicleQueue)
//			{
//				System.out.println(vehicle);
//				System.out.println("-------------- DRIVER ----------");
//				System.out.println(vehicle.driver);
//				System.out.println("-------------- PASSENGERS ----------");
//				for(Passenger p : vehicle.passengers)
//				{
//					System.out.println(p);
//				}
//				System.out.println("----------------------------------");
//			}

			
			PoliceTerminal[] policeTerminals = new PoliceTerminal[3];
			
			policeTerminals[0] = new PoliceTerminalForOthers(vehicleQueue);
			policeTerminals[1] = new PoliceTerminalForOthers(vehicleQueue);
			policeTerminals[2] = new PoliceTerminalForTrucks(vehicleQueue);
			
			List<PoliceTerminal> policeTerminalsForOthers = new ArrayList<>();
			policeTerminalsForOthers.add(policeTerminals[0]);
			policeTerminalsForOthers.add(policeTerminals[1]);
			List<PoliceTerminal> policeTerminalsForTrucks = new ArrayList<>();
			
			List<CustomsTerminal> customsTerminals = new ArrayList<>();
			CustomsTerminal ct1 = new CustomsTerminalForOthers(policeTerminalsForOthers);
			CustomsTerminal ct2 = new CustomsTerminalForTrucks(policeTerminalsForTrucks);
		
			customsTerminals.add(ct1);
			customsTerminals.add(ct2);
			
			while(vehicleQueue.size() > 0)
			{
				Vehicle<?> nextElement = vehicleQueue.peek();
				System.out.println("NEXT ELEMENT: " + nextElement);
				Thread.sleep(3000);
				for(CustomsTerminal ct : customsTerminals)
				{
					if((nextElement instanceof Automobile || nextElement instanceof Bus) && ct instanceof CustomsTerminalForOthers)
					{
						if(ct.getVehicleAtTerminal() == null)
						{
							ct.setVehicleAtTerminal(vehicleQueue.poll());
							((CustomsTerminalForOthers)ct).processVehicle();
						}
					}
					if(nextElement instanceof Truck && ct instanceof CustomsTerminalForTrucks)
					{
						if(ct.getVehicleAtTerminal() == null)
						{
							ct.setVehicleAtTerminal(vehicleQueue.poll());
							((CustomsTerminalForTrucks)ct).processVehicle();
						}
					}
				}
			}
			System.out.println("FINISHED!");
		} //end of try-block
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	} // end of start (Method)

	
	public static void main(String[] args) {
		launch(args);
	}
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////	HELPFUL METHODS	////////////////////////////////////
	private List<Vehicle<?>> fillAndShuffleList(final int NUMBER_OF_BUSES_AT_START,
			final int NUMBER_OF_TRUCKS_AT_START, final int NUMBER_OF_CARS_AT_START) {
		List<Vehicle<?>> listToShuffle = new ArrayList<>();
		
		Vehicle<BusPassenger>[] busesForList = new Bus[NUMBER_OF_BUSES_AT_START];
		Vehicle<Passenger>[] trucksForList = new Truck[NUMBER_OF_TRUCKS_AT_START];
		Vehicle<Passenger>[] automobilesForList = new Automobile[NUMBER_OF_CARS_AT_START];
		
		for(int i = 0 ; i < Math.max(Math.max(NUMBER_OF_CARS_AT_START, NUMBER_OF_TRUCKS_AT_START), NUMBER_OF_BUSES_AT_START); ++i) 
		{
			automobilesForList[i] = new Automobile();
			listToShuffle.add(automobilesForList[i]);
			if(i < NUMBER_OF_TRUCKS_AT_START)
			{
				trucksForList[i] = new Truck();
				listToShuffle.add(trucksForList[i]);
			}
			if(i < NUMBER_OF_BUSES_AT_START)
			{
				busesForList[i] = new Bus();
				listToShuffle.add(busesForList[i]);
			}
		} //for loop that initializes set number of Automobiles, Trucks and Buses 
		Collections.shuffle(listToShuffle);
		return listToShuffle;
}
}

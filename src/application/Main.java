package application;
	
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.application.Application;
import javafx.stage.Stage;
import passengers.BusPassenger;
import passengers.Passenger;
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
			
			Queue<Vehicle<?>> vehicleQueue = new LinkedBlockingQueue<>(listToShuffle);
			
			for(Vehicle<?> vehicle : vehicleQueue)
			{
				System.out.println(vehicle);
				System.out.println("-------------- DRIVER ----------");
				System.out.println(vehicle.driver);
				System.out.println("-------------- PASSENGERS ----------");
				for(Passenger p : vehicle.passengers)
				{
					System.out.println(p);
				}
				System.out.println("----------------------------------");
			}
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

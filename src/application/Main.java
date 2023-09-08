package application;
	
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.application.Application;
import javafx.stage.Stage;
import terminals.CustomsTerminal;
import terminals.CustomsTerminalForOthers;
import terminals.CustomsTerminalForTrucks;
import terminals.PoliceTerminal;
import terminals.PoliceTerminalForOthers;
import terminals.PoliceTerminalForTrucks;
import terminals.managers.PoliceTerminalsManager;
import util.random.IdentificationGenerator;
import util.random.RandomGenerator;
import vehicles.Vehicle;
import vehicles.Bus;
import vehicles.Automobile;
import vehicles.Truck;
import vehicles.documents.CustomsDocument;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import gui.BorderCrossingGUIController;


public class Main extends Application {
	public static BlockingQueue<Vehicle<?>> vehicleQueue;

	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/gui/BorderCrossingGUI.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Our Stage");
			primaryStage.show();
		}
		catch(Exception ex)
		{
			//System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
			
	} // end of start (Method)

	
	public static void main(String[] args) {
		launch(args);
	}
	
}

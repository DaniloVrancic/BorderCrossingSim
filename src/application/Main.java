package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import passengers.Passenger;
import util.random.IdentificationGenerator;
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
			
			IdentificationGenerator generator = new IdentificationGenerator();
			Passenger p1 = new Passenger(generator);
			Passenger p2 = new Passenger(generator);
			Passenger p3 = new Passenger(generator);
			Passenger p4 = new Passenger(generator);
			
			System.out.println(p1);
			System.out.println(p2);
			System.out.println(p3);
			System.out.println(p4);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

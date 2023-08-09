package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import passengers.BusPassenger;
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
			BusPassenger[] pas = new BusPassenger[20];
			
			
			for(int i = 0; i < 20; i++)
			{
				pas[i] = new BusPassenger(generator);
			}
			
			for(BusPassenger p : pas)
			{
				System.out.println(p.getFullName() + " HAS LUGGAGE: \t" + p.hasLuggage() + " HAS ILLEGAL LUGGAGE: " + p.hasIllegalLuggage());
			}
			
		} catch(Exception e) {
			e.printStackTrace(); // LOGGER
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

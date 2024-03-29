package application;
	
import java.util.concurrent.BlockingQueue;
import javafx.application.Application;
import javafx.stage.Stage;
import vehicles.Vehicle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	public static BlockingQueue<Vehicle<?>> vehicleQueue;

	String APPLICATION_ICON_URL = "https://shorturl.at/klKX9";
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/gui/BorderCrossingGUI.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Border Crossing Simulator");
			
			Image icon = new Image(APPLICATION_ICON_URL);
			if(icon != null)
		    primaryStage.getIcons().add(icon);
	
	        
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

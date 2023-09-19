package gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logger.LoggerManager;
import vehicles.Automobile;
import vehicles.Bus;
import vehicles.Truck;
import vehicles.Vehicle;

public class VehicleQueueController implements Initializable{

	final int LISTVIEW_REFRESH_RATE_TIME = 300;
	
	@FXML
	public ListView<Vehicle<?>> allVehiclesListView;
	

	
	public BlockingQueue<Vehicle<?>> vehicleQueue;
	int recordedQueueLength = 0;
	Logger errorLogger = LoggerManager.getErrorLogger();
	
	// ICON DIMENSIONS//
	int ICON_WIDTH 	= 32;
	int ICON_HEIGHT = 32;
	//
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		this.vehicleQueue = BorderCrossingGUIController.vehicleQueue;
		if(allVehiclesListView != null)
		{
			allVehiclesListView.setCellFactory(list -> new VehicleListCell());
		}
		
		//Thread that will refresh the ListView
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true)
				{
					try {
						Thread.sleep(LISTVIEW_REFRESH_RATE_TIME);
						
							Platform.runLater(() -> updateListView());
							if(vehicleQueue.isEmpty())
							{
								return;
							}
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
			}
        	
        });
        t.start();
        
        // Set a custom cell factory for the ListView
		
		
//		try
//		{
//			updateListView();
//			
//		}
//		catch(Exception ex)
//		{
//			errorLogger.severe(ex.getMessage());
//			System.out.println(ex.getMessage());
//		}			
	
		
		
	}
	
    // Define a custom cell for the ListView
    private class VehicleListCell extends ListCell<Vehicle<?>> {
        @Override
        protected void updateItem(Vehicle<?> item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Set the text to the vehicle's name
                setText(item.getClass().getSimpleName() + "\tID: " + item.getVehicleId());

                // Determine the appropriate icon based on the vehicle type
                ImageView icon = new ImageView();
                if (item instanceof Automobile) {
                    icon.setImage(new Image(getClass().getResourceAsStream("carIcon.png")));
                } else if (item instanceof Bus) {
                    icon.setImage(new Image(getClass().getResourceAsStream("busIcon.png")));
                } else if (item instanceof Truck) {
                    icon.setImage(new Image(getClass().getResourceAsStream("truckIcon.png")));
                }
                icon.setFitWidth(ICON_WIDTH);
                icon.setFitHeight(ICON_HEIGHT);
                setGraphic(icon);
            }
        }
    }
    
    public void updateListView()
    {
    	
				if (vehicleQueue != null && allVehiclesListView != null) 
				{
					if(recordedQueueLength != vehicleQueue.size())
					{
						recordedQueueLength = vehicleQueue.size();
						allVehiclesListView.getItems().clear();					
						allVehiclesListView.getItems().addAll(vehicleQueue);
					}
				}
		
    }
    
	
	
	
	
}

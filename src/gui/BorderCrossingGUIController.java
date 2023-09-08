package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import passengers.BusPassenger;
import passengers.Passenger;
import terminals.CustomsTerminal;
import terminals.PoliceTerminalForOthers;
import terminals.Terminal;
import terminals.managers.CustomsTerminalsManager;
import terminals.managers.PoliceTerminalsManager;
import util.random.IdentificationGenerator;
import vehicles.Automobile;
import vehicles.Bus;
import vehicles.Truck;
import vehicles.Vehicle;

public class BorderCrossingGUIController implements Initializable
{

	 	@FXML
	    public ListView<Vehicle<?>> topFiveListView;
	 	
	 	@FXML
	 	public Rectangle policeTerminal1Rectangle;
	 	
	 	@FXML
	 	public Rectangle policeTerminal2Rectangle;
	 	
	 	@FXML
	 	public Rectangle policeTerminalTrucksRectangle;
	 	
	 	@FXML
	 	public Rectangle customsTerminal1Rectangle;
	 	
	 	@FXML
	 	public Rectangle customsTerminalTrucksRectangle;
	 	
	 	

	    public static BlockingQueue<Vehicle<?>> vehicleQueue;
	    
	    public static boolean IS_PAUSED = false;


	    public void setVehicleQueue(BlockingQueue<Vehicle<?>> vehicleQueue) {
	        this.vehicleQueue = vehicleQueue;
	        // Update the ListView when the vehicleQueue is set
	        updateListView();
	    }

	    private void updateRectangle(Rectangle rec, Terminal t) {
	        Vehicle<?> item = t.getVehicleAtTerminal();

	        if (item == null) {
	            // Handle the case where item is null (optional)
	        } else {
	            // Create an HBox to hold the icon and text
	            HBox content = new HBox(5); // 5 is the spacing between icon and text (you can adjust this)

	            // Determine the appropriate icon based on the vehicle type
	            ImageView icon = new ImageView();
	            if (item instanceof Automobile) {
	                icon.setImage(new Image(getClass().getResourceAsStream("carIcon.png")));
	            } else if (item instanceof Bus) {
	                icon.setImage(new Image(getClass().getResourceAsStream("busIcon.png")));
	            } else if (item instanceof Truck) {
	                icon.setImage(new Image(getClass().getResourceAsStream("truckIcon.png")));
	            }
	            icon.setFitWidth(28);
	            icon.setFitHeight(28);

	            // Create a label for the text
	            Label label = new Label(item.getClass().getSimpleName() + "\tID: " + item.getVehicleId());

	            // Add the icon and label to the HBox
	            content.getChildren().addAll(icon, label);

	            // Set the content of the rectangle
	            //rec.setContent(content);
	            rec.setClip(content); //FIX LATER, NOT APPROPRIATE METHOD!!!!!!!!!!!!!!!!!!!!
	        }
	    }
	    private void updateListView() {
	        if (vehicleQueue != null) {
	            topFiveListView.getItems().clear();
	            // Add the top 5 vehicles to the ListView
	            int count = 0;
	            for (Vehicle<?> vehicle : vehicleQueue) {
	                topFiveListView.getItems().add(vehicle);
	                count++;
	                if (count >= 5) {
	                    break;
	                }
	            }

	            // Add empty cells if needed
	            while (count < 5) {
	                topFiveListView.getItems().add(null);
	                count++;
	            }
	        }
	    }
	    
	    private void updateScene()
	    {
	    	updateListView();
	    	updateRectangle(policeTerminal1Rectangle, PoliceTerminalsManager.availablePoliceTerminalsForOthers.get(0));
	    	updateRectangle(policeTerminal2Rectangle, PoliceTerminalsManager.availablePoliceTerminalsForOthers.get(1));
	    	updateRectangle(policeTerminalTrucksRectangle, PoliceTerminalsManager.availablePoliceTerminalsForTrucks.get(0));
	    	updateRectangle(customsTerminal1Rectangle, CustomsTerminalsManager.availableCustomsTerminalsForOthers.get(0));
	    	updateRectangle(customsTerminalTrucksRectangle, CustomsTerminalsManager.availableCustomsTerminalsForTrucks.get(0));
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
	                icon.setFitWidth(28);
	                icon.setFitHeight(28);
	                setGraphic(icon);
	            }
	        }
	    }

		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			final int NUMBER_OF_BUSES_AT_START 		= 5;
			final int NUMBER_OF_TRUCKS_AT_START 	= 10;
			final int NUMBER_OF_CARS_AT_START 		= 35;
			/////////////////////////////////

			try {
			
			//IdentificationGenerator generator = new IdentificationGenerator();
			List<Vehicle<?>> listToShuffle = fillAndShuffleList(NUMBER_OF_BUSES_AT_START, NUMBER_OF_TRUCKS_AT_START,
					NUMBER_OF_CARS_AT_START);
			
			this.vehicleQueue = new LinkedBlockingQueue<>(listToShuffle);

			
			
			List<CustomsTerminal> customsTerminals = new ArrayList<>();
			
		        

		        // Start vehicle threads
		        
		            Vehicle<?> vehicle = vehicleQueue.peek();
		            Thread vehicleThread = new Thread(vehicle);
		            vehicleThread.start();
		        

		        // Wait for all vehicle threads to finish
//		        for (Thread thread : vehicleThreads) {
//		            try {
//		                thread.join();
//		            } catch (InterruptedException e) {
//		                e.printStackTrace();
//		            }
//		        }
			System.out.println("FINISHED!");
		} //end of try-block
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
			
			 // Initialize the ListView with the top 5 vehicles from the queue
			topFiveListView.setCellFactory(list -> new VehicleListCell());
			//updateListView();
	        
	        Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					while(true)
					{
						try {
							Thread.sleep(500);
							synchronized(vehicleQueue) {
								Platform.runLater(() -> updateScene());
								if(vehicleQueue.isEmpty())
								{
									return;
								}
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
			
		}
	
	

////////////////////////////////////////////////////////////////////////////////
////////////////////////	HELPFUL METHODS	////////////////////////////////////
	private List<Vehicle<?>> fillAndShuffleList(final int NUMBER_OF_BUSES_AT_START,
				final int NUMBER_OF_TRUCKS_AT_START, final int NUMBER_OF_CARS_AT_START) {
			List<Vehicle<?>> listToShuffle = new ArrayList<>();

			Vehicle<BusPassenger>[] busesForList = new Bus[NUMBER_OF_BUSES_AT_START];
			Vehicle<Passenger>[] trucksForList = new Truck[NUMBER_OF_TRUCKS_AT_START];
			Vehicle<Passenger>[] automobilesForList = new Automobile[NUMBER_OF_CARS_AT_START];

			for (int i = 0; i < Math.max(Math.max(NUMBER_OF_CARS_AT_START, NUMBER_OF_TRUCKS_AT_START),
					NUMBER_OF_BUSES_AT_START); ++i) {
				automobilesForList[i] = new Automobile();
				listToShuffle.add(automobilesForList[i]);
				if (i < NUMBER_OF_TRUCKS_AT_START) {
					trucksForList[i] = new Truck();
					listToShuffle.add(trucksForList[i]);
				}
				if (i < NUMBER_OF_BUSES_AT_START) {
					busesForList[i] = new Bus();
					listToShuffle.add(busesForList[i]);
				}
			} // for loop that initializes set number of Automobiles, Trucks and Buses
			Collections.shuffle(listToShuffle);
			return listToShuffle;
		}
}

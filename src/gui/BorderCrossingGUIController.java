package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import logger.LoggerManager;
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
	 	public Pane policeTerminal1Pane;
	 	
	 	@FXML
	 	public Pane policeTerminal2Pane;
	 	
	 	@FXML
	 	public Pane policeTerminalTrucksPane;
	 	
	 	@FXML
	 	public Pane customsTerminal1Pane;
	 	
	 	@FXML
	 	public Pane customsTerminalTrucksPane;
	 	
	 	

	    public static BlockingQueue<Vehicle<?>> vehicleQueue;
	    public static HashMap<Terminal,Pane> terminalToPaneMap;
	    
	    public static boolean IS_PAUSED = false;
	    
	    List<Terminal> allTerminals = new ArrayList<>();


	    public void setVehicleQueue(BlockingQueue<Vehicle<?>> vehicleQueue) {
	        this.vehicleQueue = vehicleQueue;
	        // Update the ListView when the vehicleQueue is set
	        updateListView();
	    }

	    private void updatePane(Terminal t) {
	    	
	    	Pane pane = terminalToPaneMap.get(t);
	        Vehicle<?> item = t.getVehicleAtTerminal();
	        
	        if (item == null) {
	            
	            // Set the background color, border, etc. back to their default values
	            pane.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-width: 0.8;");
	            pane.getChildren().clear(); // Clear any existing content
	        } else {
	        	drawPaneWithVehicle(pane, item);
	        }
	    }
	    
	    private void updatePane(Pane pane) {
	    	Terminal t = null;
	    	for(Map.Entry<Terminal,Pane> entry : terminalToPaneMap.entrySet()) //Find the Terminal that corresponds to the pane
	    	{
	    		if(entry.getValue().equals(pane))
	    		{
	    			t = entry.getKey();
	    		}
	    	}
	    	
	    	if(t == null)
	    	{
	    		throw new NullPointerException("The terminal to the corresponding pane has not been found!");
	    	}
	    	
	        Vehicle<?> item = t.getVehicleAtTerminal();
	        
	        if (item == null) {
	            
	            // Set the background color, border, etc. back to their default values
	            pane.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-width: 0.8;");
	            pane.getChildren().clear(); // Clear any existing content
	        } else {
	            
	            drawPaneWithVehicle(pane, item);
	        }
	    }

		private void drawPaneWithVehicle(Pane pane, Vehicle<?> item) {
			HBox content = new HBox(5);
			content.setStyle("-fx-background-color: transparent;");
			
			// Determine the appropriate icon based on the vehicle type
			ImageView icon = new ImageView();
			if (item instanceof Automobile) {
			    icon.setImage(new Image(getClass().getResourceAsStream("carIcon.png")));
			} else if (item instanceof Bus) {
			    icon.setImage(new Image(getClass().getResourceAsStream("busIcon.png")));
			} else if (item instanceof Truck) {
			    icon.setImage(new Image(getClass().getResourceAsStream("truckIcon.png")));
			}
			
			icon.setFitWidth(32);
			icon.setFitHeight(32);
         

			// Create a label for the text
			Label label = new Label(item.getClass().getSimpleName() + "\nID: " + item.getVehicleId());
			label.setWrapText(true);  // Enable text wrapping
			label.setTextFill(Color.BLACK);
			label.setAlignment(Pos.CENTER);
			

			content.setAlignment(Pos.CENTER);
			HBox.setHgrow(content, Priority.ALWAYS);

			// Adjust the spacing and padding as needed
			content.setSpacing(10);  // Adjust the spacing between the icon and label
			content.setPadding(new Insets(10, 10, 10, 10));  // Add some padding
			// Set the content of the pane to the HBox
			content.getChildren().addAll(icon, label);
			pane.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000; -fx-border-width: 0.8;");
			
			pane.getChildren().setAll(content); // Set the content of the pane
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
	    
	    private void updateScene() {
	        updateListView();
	        
	        for(Terminal t : terminalToPaneMap.keySet())
	        {
	       	updatePane(t);	        	
	        }
	       
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
			List<Terminal> allTerminals = new ArrayList<>();
			allTerminals.addAll(PoliceTerminalsManager.availablePoliceTerminals);
			allTerminals.addAll(CustomsTerminalsManager.availableCustomsTerminals);

			try
			{
				terminalToPaneMap = new HashMap<>();
				terminalToPaneMap.put(PoliceTerminalsManager.availablePoliceTerminals.get(0), policeTerminal1Pane);
				terminalToPaneMap.put(PoliceTerminalsManager.availablePoliceTerminals.get(1), policeTerminal2Pane);
				terminalToPaneMap.put(PoliceTerminalsManager.availablePoliceTerminals.get(2), policeTerminalTrucksPane);
				terminalToPaneMap.put(CustomsTerminalsManager.availableCustomsTerminals.get(0), customsTerminal1Pane);
				terminalToPaneMap.put(CustomsTerminalsManager.availableCustomsTerminals.get(1), customsTerminalTrucksPane);
			}
			catch (Exception ex)
			{
				Logger errorLogger = LoggerManager.getErrorLogger();
				errorLogger.severe(ex.getMessage());
			}
			
			//policeTerminal1Pane.getChildren().addListener((ListChangeListener<Node>) change -> { updatePane(policeTerminal1Pane);});
			//policeTerminal2Pane.getChildren().addListener((ListChangeListener<Node>) change -> { updatePane(policeTerminal2Pane);});
			//policeTerminalTrucksPane.getChildren().addListener((ListChangeListener<Node>) change -> { updatePane(policeTerminalTrucksPane);});
			//customsTerminal1Pane.getChildren().addListener((ListChangeListener<Node>) change -> { updatePane(customsTerminal1Pane);});
			//customsTerminalTrucksPane.getChildren().addListener((ListChangeListener<Node>) change -> { updatePane(customsTerminalTrucksPane);});
			
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
							Thread.sleep(100);
							synchronized(vehicleQueue) {
								Platform.runLater(() -> updateScene());
								if(vehicleQueue.isEmpty() && allTerminalsEmpty(allTerminals))
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
	
		private boolean allTerminalsEmpty(List<Terminal> allTerminals)
		{
			allTerminals.addAll(PoliceTerminalsManager.availablePoliceTerminals);
			allTerminals.addAll(CustomsTerminalsManager.availableCustomsTerminals);
			
			for(Terminal t : allTerminals)
			{
				if(t.getVehicleAtTerminal() != null)
				{
					return false;
				}
			}
			return true;
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
	
	public static void colorPaneofTerminal(Terminal terminal, Color color) {
	    Pane paneToColor = terminalToPaneMap.get(terminal);
	    
	    
	    
	    // Convert the Color object to a CSS-friendly format
	    String cssColor = String.format("#%02X%02X%02X",
	            (int)(color.getRed() * 255),
	            (int)(color.getGreen() * 255),
	            (int)(color.getBlue() * 255));

	    paneToColor.setStyle("-fx-background-color: " + cssColor + ";");
	    for(Node n : paneToColor.getChildren())
	    	{
	    	 if(n instanceof ImageView || n instanceof Label)
	    	 {
	    		 n.setStyle("-fx-background-color: " + cssColor + ";");
	    	 }
	    	}
	    
	}
}

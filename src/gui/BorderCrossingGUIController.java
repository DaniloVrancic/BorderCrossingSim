package gui;

import java.io.IOException;
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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
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
	///////////////////////////////////////
	final int BORDER_CROSSING_GUI_REFRESH_RATE_TIME = 100;
	/// VALUES THAT SERVE OPTIMIZATION ---
	public static boolean listViewNeedsRefresh;
	public static boolean terminalsNeedRefresh;
	/// VALUES THAT SERVE OPTIMIZATION ---
	/// SINGLETON PATTERN - FOR ACCESSING THE COMPONENTS FROM OTHER CLASSES -----------
	private static BorderCrossingGUIController instance;
	public static BorderCrossingGUIController getInstance() {
        return instance;
    }
	
	////////////////////////////////////////////////////////////////////////////
	//////////////////////////////	NODES AND GUI COMPONENTS	////////////////
	
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
	 	
	 	@FXML
	 	public TextArea selectedVehicleInfoTextArea;
	 	
	 	@FXML
	 	public TextArea relevantEventsTextArea;
	 	
	 	@FXML
	 	public Label durationLabel;
	 	
	 	///////////////////////////////////////////////
	 	
	 	///////////////////////////////////////////////

	    public static BlockingQueue<Vehicle<?>> vehicleQueue;	//The queue of vehicles 
	    public static HashMap<Terminal,Pane> terminalToPaneMap;
	    
	    public static boolean IS_PAUSED = false;
	    
	    List<Terminal> allTerminals = new ArrayList<>();
	    private Vehicle<?> selectedVehicle; //When clicking on the vehicles on screen, will serve as a placeholder for the selected vehicle
	    
	    //FOR KEEPING TRACK OF THE TIME
	    private Timeline timeline;
	    private int secondsElapsed = 0;
	    // FOR KEEPING TRACK OF THE TIME

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
	    
	    private void updateSelectedVehicleInfoTextArea()
	    {
	    	if(selectedVehicle == null)
	    	{
	    		selectedVehicleInfoTextArea.clear();
	    	}
	    	else
	    	{
	    		selectedVehicleInfoTextArea.clear();
	    		String textToDisplay = selectedVehicle.toString(); //TEST THIS!!!!!!!!!!!!!!!!!!!
	    		selectedVehicleInfoTextArea.setText(textToDisplay);
	    	}
	    }
	    
	    public void updateRelevantEventsTextArea(String newEventText)
	    {
	    	StringBuffer sb = new StringBuffer();
	    	
	    	sb.append(newEventText);
	    	sb.append("\n----------------\n");
	    	this.relevantEventsTextArea.appendText(sb.toString());
	    }
	    
	    private void updateScene() {
	    	
	    	if(listViewNeedsRefresh)
	    	{
	    		synchronized (topFiveListView) {
	    			updateListView();				
	    			listViewNeedsRefresh = false;					
				}
	    	}
	        
	    	if(terminalsNeedRefresh)
	    	{
	    		for(Terminal t : terminalToPaneMap.keySet())
	    		{
	    			updatePane(t);	        	
	    		}
	    		terminalsNeedRefresh = false;
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
			instance = this;
			
			selectedVehicle = null;
			List<Terminal> allTerminals = new ArrayList<>();
			allTerminals.addAll(PoliceTerminalsManager.availablePoliceTerminals);
			allTerminals.addAll(CustomsTerminalsManager.availableCustomsTerminals);

			listViewNeedsRefresh = true;
			terminalsNeedRefresh = true;
			try  //MAPS THE TERMINALS TO THEIR CORRESPONDING PANES ON THE GUI
			{
				terminalToPaneMap = new HashMap<>();
				terminalToPaneMap.put(PoliceTerminalsManager.availablePoliceTerminals.get(0), policeTerminal1Pane);
				terminalToPaneMap.put(PoliceTerminalsManager.availablePoliceTerminals.get(1), policeTerminal2Pane);
				terminalToPaneMap.put(PoliceTerminalsManager.availablePoliceTerminals.get(2), policeTerminalTrucksPane);
				terminalToPaneMap.put(CustomsTerminalsManager.availableCustomsTerminals.get(0), customsTerminal1Pane);
				terminalToPaneMap.put(CustomsTerminalsManager.availableCustomsTerminals.get(1), customsTerminalTrucksPane);
				
				mapTerminalToPane(PoliceTerminalsManager.availablePoliceTerminals.get(0), policeTerminal1Pane);
				mapTerminalToPane(PoliceTerminalsManager.availablePoliceTerminals.get(1), policeTerminal2Pane);
				mapTerminalToPane(PoliceTerminalsManager.availablePoliceTerminals.get(2), policeTerminalTrucksPane);
				mapTerminalToPane(CustomsTerminalsManager.availableCustomsTerminals.get(0), customsTerminal1Pane);
				mapTerminalToPane(CustomsTerminalsManager.availableCustomsTerminals.get(1), customsTerminalTrucksPane);
				
				
			} //end of try block
			catch (Exception ex)
			{
				Logger errorLogger = LoggerManager.getErrorLogger();
				errorLogger.severe(ex.getMessage());
			} //end of catch block
			
			
			
			try {
			List<Vehicle<?>> listToShuffle = fillAndShuffleList(NUMBER_OF_BUSES_AT_START, NUMBER_OF_TRUCKS_AT_START,
					NUMBER_OF_CARS_AT_START);
			
			vehicleQueue = new LinkedBlockingQueue<>(listToShuffle);
		        // Start vehicle threads
		        
		            Vehicle<?> vehicle = vehicleQueue.peek();
		            Thread vehicleThread = new Thread(vehicle);
		            vehicleThread.start();
		        
		            //System.out.println("FINISHED!");
		} //end of try-block
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
			
			 // Initialize the ListView with the top 5 vehicles from the queue
			topFiveListView.setCellFactory(list -> new VehicleListCell());
			//updateListView();
			
			//SETUP TIMER
			timeline = new Timeline(new KeyFrame(Duration.seconds(1), this::updateDurationLabel));
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.play();
			
			//SETUP TIMER
	        
	        Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					while(true)
					{
						try {
							Thread.sleep(BORDER_CROSSING_GUI_REFRESH_RATE_TIME);
							
								Platform.runLater(() -> updateScene());
								if(vehicleQueue.isEmpty() && allTerminalsEmpty(allTerminals))
								{
									timeline.pause();
									
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
	
    private void updateDurationLabel(ActionEvent event) {
        secondsElapsed++;
        int hours = secondsElapsed / 3600;
        int minutes = (secondsElapsed % 3600) / 60;
        int seconds = secondsElapsed % 60;
        

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        durationLabel.setText(timeString);
    }
	
	private static void mapTerminalToPane(Terminal terminal, Pane pane)
	{
		pane.getProperties().put(pane, terminal);
	}
	
	private static Terminal getTerminalFromPane(Pane pane)
	{
		return (Terminal)pane.getProperties().get(pane);
	}
	
	public void selectVehicleFromTerminal(MouseEvent event)
	{
		Pane selectedPane = (Pane)event.getSource();
		Terminal t = getTerminalFromPane(selectedPane);
		this.selectedVehicle = t.getVehicleAtTerminal();
		updateSelectedVehicleInfoTextArea();
	}
	
	
	public void selectVehicleFromList(MouseEvent event) {
		
			this.selectedVehicle = (Vehicle<?>) topFiveListView.getSelectionModel().getSelectedItem();
			System.out.println(selectedVehicle);
			updateSelectedVehicleInfoTextArea();			
		
	}
	
	@FXML
	public void handleVehicleQueueButtonClick(ActionEvent event) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/VehicleQueue.fxml"));
	        Parent root = loader.load();

	        Stage stage = new Stage();
	        stage.setTitle("Vehicle Queue");
	        stage.setScene(new Scene(root));

	        // Set the controller for the new window
	        //VehicleQueueController controller = loader.getController();
	        //controller.initialize(null,null);
	        Platform.runLater(() -> {
	            stage.show();
	        });
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	

}

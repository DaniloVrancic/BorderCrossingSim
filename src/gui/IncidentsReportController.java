package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import vehicles.Vehicle;

public class IncidentsReportController implements Initializable{

	@FXML
	private TreeView<Vehicle<?>> incidentTreeView;
	
	@FXML
	private CheckBox passedWithIncidentCheckBox;
	
	@FXML
	private CheckBox didNotPassCheckBox;
	
	@FXML
	private TextArea incidentTextArea;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//USE PUNISHED PERSON MANAGER TO GET ALL THE DATA THAT IS NECESSARRY ABOUT THE VEHICLES!
		//ALSO ADD PUNISHEDMENT FOR THE DRIVERS (ADD THEM WITH PunishedPersonManager) AS WELL, AFTER USING StoppedVehicleManager on them!
	}

}

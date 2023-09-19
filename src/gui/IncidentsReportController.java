package gui;

import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.KeyValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import passengers.PunishedPassenger;
import passengers.PunishmentManager;
import vehicles.PunishedVehicle;
import vehicles.Vehicle;
import custom_interfaces.Punishable;

public class IncidentsReportController implements Initializable{

	
	////////////// ICON LOCATIONS ////////////////////
	private String manIcon 		= "man.png";
	private String womanIcon 	= "woman.png";
	private String carIcon		= "carIcon.png";
	private String busIcon		= "busIcon.png";
	private String truckIcon	= "truckIcon.png";
	//////////////////////////////////////////////////
	////////////// IMAGES FOR TREEVIEW ///////////////
	
	////////////// COMPONENTS	  ////////////////////
	@FXML
	private TreeView<Serializable> incidentTreeView;
	
	@FXML
	private CheckBox passedWithIncidentCheckBox;
	
	@FXML
	private CheckBox didNotPassCheckBox;
	
	@FXML
	private TextArea incidentTextArea;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		incidentTextArea.clear();
		TreeItem<Serializable> root = new TreeItem<>("Root Item"); //Not good, but for testing purposes
		TreeItem<Serializable> treeItemVehicleToAdd;
		TreeItem<Serializable> treeItemPassengerToAdd;
		Map<PunishedVehicle, List<PunishedPassenger>> deserializedHashMap = PunishmentManager.allPunishmentsMap;
		// TEST PURPOSES USING THE FROM MEMORY MAP, WILL USE DESERIALIZE LATER
		
		for(Map.Entry<PunishedVehicle,List<PunishedPassenger>> entry : deserializedHashMap.entrySet())
		{
			treeItemVehicleToAdd = new TreeItem<>(entry.getKey());
			root.getChildren().add(treeItemVehicleToAdd);
			List<PunishedPassenger> retrievedList = entry.getValue();
			if(retrievedList != null)
			{
				for(PunishedPassenger p_passenger : retrievedList)
				{
					treeItemPassengerToAdd = new TreeItem<>(p_passenger);
					treeItemVehicleToAdd.getChildren().add(treeItemPassengerToAdd);
				}				
			}
			
		}
		
		incidentTreeView.setShowRoot(false);
		incidentTreeView.setRoot(root);
	}
	
	@FXML
	public void selectItem() {
		TreeItem<Serializable> selectedItem = incidentTreeView.getSelectionModel().getSelectedItem();
		
		if(selectedItem != null) //if anything else other than the treeItems have been clicked
		{
			StringBuffer sb = new StringBuffer();
			Serializable selectedValue = selectedItem.getValue();
			if(selectedValue instanceof PunishedVehicle)
			{
				PunishedVehicle retrievedVehicle = (PunishedVehicle) selectedValue;
				if(retrievedVehicle.getReasonOfPunishment() != null && retrievedVehicle.getReasonOfPunishment().length() > 0)
				{
					sb.append("------------------------------\n");
					sb.append("REASON OF PUNISHMENT:\n");
					sb.append(retrievedVehicle.getReasonOfPunishment());
					sb.append("\n");
				}
				sb.append("------------------------------\n");
				sb.append("VEHICLE INFORMATION:\n");
				sb.append(retrievedVehicle.getVehicle());
				incidentTextArea.setText(sb.toString());
			}
			else if(selectedValue instanceof PunishedPassenger)
			{
				PunishedPassenger retrievedPassenger = (PunishedPassenger) selectedValue;
				if(retrievedPassenger.getReasonOfPunishment() != null && retrievedPassenger.getReasonOfPunishment().length() > 0)
				{
					sb.append("------------------------------\n");
					sb.append("REASON OF PUNISHMENT:\n");
					sb.append(retrievedPassenger.getReasonOfPunishment());
					sb.append("\n");
				}
				sb.append("------------------------------\n");
				sb.append("PASSENGER INFORMATION:\n");
				sb.append("FULL NAME: " + retrievedPassenger.document.getFullName() + "\n");
				sb.append("PASSPORT NUMBER: " + retrievedPassenger.document.getPassportNumber() + "\n");
				sb.append("GENDER: " + retrievedPassenger.document.getGender() + "\n");
				sb.append("NATIONALITY: " + retrievedPassenger.document.getNationality());
				incidentTextArea.setText(sb.toString());
			}
		}
		else
		{
			incidentTextArea.clear();
		}
		
	}

}

package gui;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import passengers.PunishedPassenger;
import passengers.PunishmentManager;
import vehicles.PunishedVehicle;
import vehicles.seriazilabledatastructure.SerializableHashMap;

public class IncidentsReportController implements Initializable{

	
	private String SERIALIZATION_FILES_DIRECTORY = "serialization/passengers";
	File[] files;

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
		File directory = new File(SERIALIZATION_FILES_DIRECTORY);
		files = directory.listFiles();
		for(File file : files)
		{
			System.out.println(file);
		}
		TreeItem<Serializable> root = new TreeItem<>("List Report"); //Not good, but for testing purposes
		TreeItem<Serializable> currentInstanceTreeItem = null; //Data retrieved from the current application instance, from RAM
		Map<PunishedVehicle, List<PunishedPassenger>> deserializedHashMap = PunishmentManager.allPunishmentsMap;
		
		
		for (TreeItem<Serializable> item : root.getChildren()) {
            if (item.getValue().toString().equals("CURRENT INSTANCE")) {
            	currentInstanceTreeItem = item;
                break;
            }
        }
		
		if (currentInstanceTreeItem == null) {
			currentInstanceTreeItem = new TreeItem<>("CURRENT INSTANCE");
            root.getChildren().add(currentInstanceTreeItem);
        }
		
		showEverythingOnTreeView(root, currentInstanceTreeItem, deserializedHashMap);
		
		incidentTreeView.setShowRoot(true);
		incidentTreeView.setRoot(root);
		
		passedWithIncidentCheckBox.setOnAction(e -> updateTreeView());
	    didNotPassCheckBox.setOnAction(e -> updateTreeView());
	}

	private void showEverythingOnTreeView(TreeItem<Serializable> root, TreeItem<Serializable> currentInstanceTreeItem,
			Map<PunishedVehicle, List<PunishedPassenger>> deserializedHashMap) {
		TreeItem<Serializable> treeItemForData;
		TreeItem<Serializable> treeItemVehicleToAdd;
		TreeItem<Serializable> treeItemPassengerToAdd;
		for(Map.Entry<PunishedVehicle,List<PunishedPassenger>> entry : deserializedHashMap.entrySet())
		{
			treeItemVehicleToAdd = new TreeItem<>(entry.getKey());
			currentInstanceTreeItem.getChildren().add(treeItemVehicleToAdd);
			List<PunishedPassenger> retrievedList = entry.getValue();
			if(retrievedList != null)
			{
				for(PunishedPassenger p_passenger : retrievedList)
				{
					treeItemPassengerToAdd = new TreeItem<>(p_passenger);
					treeItemVehicleToAdd.getChildren().add(treeItemPassengerToAdd);
				}				
			}
		} //end of for
		
		
		for(File file : files)
		{
			deserializedHashMap = new SerializableHashMap<>(file.getAbsolutePath());
			treeItemForData = new TreeItem<>(file.getName());
			for(Map.Entry<PunishedVehicle,List<PunishedPassenger>> entry : deserializedHashMap.entrySet())
			{
				treeItemVehicleToAdd = new TreeItem<>(entry.getKey());
				treeItemForData.getChildren().add(treeItemVehicleToAdd);
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
			root.getChildren().add(treeItemForData);
		} //end of for
	}//end of method
	
	private void updateTreeView() {
		TreeItem<Serializable> root = new TreeItem<>("List Report");
	    TreeItem<Serializable> currentInstanceTreeItem = null;
	    Map<PunishedVehicle, List<PunishedPassenger>> deserializedHashMap = PunishmentManager.allPunishmentsMap;

	    for (TreeItem<Serializable> item : root.getChildren()) {
	        if (item.getValue().toString().equals("CURRENT INSTANCE")) {
	            currentInstanceTreeItem = item;
	            break;
	        }
	    }

	    if (currentInstanceTreeItem == null) {
	        currentInstanceTreeItem = new TreeItem<>("CURRENT INSTANCE");
	        root.getChildren().add(currentInstanceTreeItem);
	    }
	    if(didNotPassCheckBox.isSelected() && passedWithIncidentCheckBox.isSelected())
	    {
	    	showEverythingOnTreeView(root, currentInstanceTreeItem, deserializedHashMap);
	    }
	    else if(didNotPassCheckBox.isSelected())
	    {
	    	showDidntPassVehicles(root, currentInstanceTreeItem, deserializedHashMap);
	    }
	    else if(passedWithIncidentCheckBox.isSelected())
	    {
	    	showPassedWithIncident(root, currentInstanceTreeItem, deserializedHashMap);
	    }
	    else
	    {
	    	showBlankFileNames(root, currentInstanceTreeItem, deserializedHashMap);
	    }


	    incidentTreeView.setShowRoot(true);
	    incidentTreeView.setRoot(root);
	}

	/**
	 * Lists only the file names that exist
	 * @param root
	 * @param currentInstanceTreeItem
	 * @param deserializedHashMap
	 */
	private void showBlankFileNames(TreeItem<Serializable> root, TreeItem<Serializable> currentInstanceTreeItem,
			Map<PunishedVehicle, List<PunishedPassenger>> deserializedHashMap) {
		TreeItem<Serializable> treeItemForData;
		for(File file : files)
		{
			deserializedHashMap = new SerializableHashMap<>(file.getAbsolutePath());
			treeItemForData = new TreeItem<>(file.getName());
			root.getChildren().add(treeItemForData);
		} //end of for
		
	}//end of method

	/**
	 * Shows only Vehicles that were thrown out and didn't pass
	 * @param root
	 * @param currentInstanceTreeItem
	 * @param deserializedHashMap
	 */
	private void showDidntPassVehicles(TreeItem<Serializable> root, TreeItem<Serializable> currentInstanceTreeItem,
			Map<PunishedVehicle, List<PunishedPassenger>> deserializedHashMap) {
		
		TreeItem<Serializable> treeItemForData;
		TreeItem<Serializable> treeItemVehicleToAdd;
		TreeItem<Serializable> treeItemPassengerToAdd;
		
		for(Map.Entry<PunishedVehicle,List<PunishedPassenger>> entry : deserializedHashMap.entrySet())
		{
			PunishedVehicle punishedVehicle = entry.getKey();
			if(punishedVehicle.getVehicle().isPassed())
			{
				continue; //If the vehicle has passed, skip and go next cause in this case we aren't interested in the ones that passed
			}
			treeItemVehicleToAdd = new TreeItem<>(entry.getKey());
			currentInstanceTreeItem.getChildren().add(treeItemVehicleToAdd);
			List<PunishedPassenger> retrievedList = entry.getValue();
			if(retrievedList != null)
			{
				for(PunishedPassenger p_passenger : retrievedList)
				{
					treeItemPassengerToAdd = new TreeItem<>(p_passenger);
					treeItemVehicleToAdd.getChildren().add(treeItemPassengerToAdd);
				}				
			}
		} //end of for
		
		
		for(File file : files)
		{
			deserializedHashMap = new SerializableHashMap<>(file.getAbsolutePath());
			treeItemForData = new TreeItem<>(file.getName());
			for(Map.Entry<PunishedVehicle,List<PunishedPassenger>> entry : deserializedHashMap.entrySet())
			{
			
				PunishedVehicle punishedVehicle = entry.getKey();
				if(punishedVehicle.getVehicle().isPassed())
				{
					continue; //If the vehicle has passed, skip and go next cause in this case we aren't interested in the ones that passed
				}
				treeItemVehicleToAdd = new TreeItem<>(entry.getKey());
				treeItemForData.getChildren().add(treeItemVehicleToAdd);
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
			root.getChildren().add(treeItemForData);
		} //end of for
	}//end of method
	/**
	 * Shows only Vehicles that passed but had an incident involving them happen.
	 * @param root
	 * @param currentInstanceTreeItem
	 * @param deserializedHashMap
	 */
	
	private void showPassedWithIncident(TreeItem<Serializable> root, TreeItem<Serializable> currentInstanceTreeItem,
			Map<PunishedVehicle, List<PunishedPassenger>> deserializedHashMap) {
		
		TreeItem<Serializable> treeItemForData;
		TreeItem<Serializable> treeItemVehicleToAdd;
		TreeItem<Serializable> treeItemPassengerToAdd;
		for(Map.Entry<PunishedVehicle,List<PunishedPassenger>> entry : deserializedHashMap.entrySet())
		{
			PunishedVehicle punishedVehicle = entry.getKey();
			if(!punishedVehicle.getVehicle().isPassed())
			{
				continue; //If the vehicle has NOT passed, skip and go next cause in this case we aren't interested in the ones that haven't passed
			}
			treeItemVehicleToAdd = new TreeItem<>(punishedVehicle);
			currentInstanceTreeItem.getChildren().add(treeItemVehicleToAdd);
			List<PunishedPassenger> retrievedList = entry.getValue();
			if(retrievedList != null)
			{
				for(PunishedPassenger p_passenger : retrievedList)
				{
					treeItemPassengerToAdd = new TreeItem<>(p_passenger);
					treeItemVehicleToAdd.getChildren().add(treeItemPassengerToAdd);
				}				
			}
		} //end of for
		
		
		for(File file : files)
		{
			deserializedHashMap = new SerializableHashMap<>(file.getAbsolutePath());
			treeItemForData = new TreeItem<>(file.getName());
			for(Map.Entry<PunishedVehicle,List<PunishedPassenger>> entry : deserializedHashMap.entrySet())
			{
			
				PunishedVehicle punishedVehicle = entry.getKey();
				if(!punishedVehicle.getVehicle().isPassed())
				{
					continue; //If the vehicle has NOT passed, skip and go next cause in this case we aren't interested in the ones that haven't passed
				}
				treeItemVehicleToAdd = new TreeItem<>(entry.getKey());
				treeItemForData.getChildren().add(treeItemVehicleToAdd);
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
			root.getChildren().add(treeItemForData);
		} //end of for
	}//end of method
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

module Border_Crossing_Simulator {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.logging;
	
	exports gui;
	
	opens application to javafx.graphics, javafx.fxml;
	opens gui;
}

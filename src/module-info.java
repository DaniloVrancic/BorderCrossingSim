module Border_Crossing_Simulator {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.logging;
	
	opens application to javafx.graphics, javafx.fxml;
}

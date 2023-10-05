package admin.subcomponents.tabs.results.histogram.consistency;

import engine.simulation.execution.SimulationExecution;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ConsistencyComponentController {

    @FXML
    //private Label consistencyText;
    private TextArea consistencyText;
    public void presentConsistency(SimulationExecution simulationExecution, String entityName, String propertyName) {
        String text = "Consistency average: \n";

        String avg = simulationExecution.averageChangesInProperty(entityName, propertyName);
        text += avg;
        consistencyText.setText(text);
    }
    public void clearTxt(){
        consistencyText.setText("");
    }
}

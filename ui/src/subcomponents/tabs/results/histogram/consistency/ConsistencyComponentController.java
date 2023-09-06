package subcomponents.tabs.results.histogram.consistency;

import engine.simulation.execution.SimulationExecution;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ConsistencyComponentController {

    @FXML
    private TextArea consistencyText;

    public void presentConsistency(SimulationExecution simulationExecution, String entityName, String propertyName) {
        String text = "Consistency average: \n";

    }
}

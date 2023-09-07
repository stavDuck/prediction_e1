package subcomponents.tabs.results.histogram.average;

import engine.entity.EntityInstance;
import engine.simulation.execution.SimulationExecution;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.List;

public class AverageValueComponentController {

    @FXML
    private Label averageText;

    public void presentAveragePropertyValue(SimulationExecution simulationExecution, String entityName, String propertyName) {
        String text = "Property average value: ";
        Float sum = 0.0f;
        Float endPopulation = 0.0f;
        List<EntityInstance> entityInstances = simulationExecution.getWorld().getInstanceManager().getInstancesByName(entityName);
        for (EntityInstance entityInstance : entityInstances) {
            sum += (Float) entityInstance.getPropertyInstanceByName(propertyName).getVal();
            endPopulation++;
        }
        if (endPopulation == 0) {
            text += "N/A"; // Display "N/A" if there are no valid values
        } else {
            // Format the result to two decimal places (adjust as needed)
            text += String.format("%.2f", sum / endPopulation);
        }

        averageText.setText(text);
    }

    public void clearTxt(){
        averageText.setText("");
    }
}
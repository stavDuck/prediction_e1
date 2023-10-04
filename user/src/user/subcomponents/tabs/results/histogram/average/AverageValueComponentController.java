package user.subcomponents.tabs.results.histogram.average;

import engine.entity.EntityInstance;
import engine.simulation.execution.SimulationExecution;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.List;

public class AverageValueComponentController {

    @FXML
    //private Label averageText;
    private TextArea averageText;

    public void presentAveragePropertyValue(SimulationExecution simulationExecution, String entityName, String propertyName) {

        String text = "Property average value: ";
        text += simulationExecution.averagePropertyValue(entityName, propertyName);
        averageText.setText(text);
    }

    public void clearTxt(){
        averageText.setText("");
    }
}
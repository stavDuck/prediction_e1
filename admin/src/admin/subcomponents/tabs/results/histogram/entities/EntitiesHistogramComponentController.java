package admin.subcomponents.tabs.results.histogram.entities;

import engine.simulation.execution.SimulationExecution;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.List;
import java.util.Map;

public class EntitiesHistogramComponentController {

    @FXML
    //private Label entitiesText;
    private TextArea entitiesText;
    public void presentHistogramByEntityAndValue(SimulationExecution simulationExecution, String entityName, String propertyName){
        String text = "";
      /*  Map<Object, Long> countByPropertyValue = simulationExecution.getWorld().getInstanceManager().getInstancesByName(entityName).stream()
                .collect(Collectors.groupingBy(
                        instance -> instance.getPropertyInstanceByName(propertyName).getVal(),
                        Collectors.counting()));

        List<Map.Entry<Object, Long>> sortedEntries = countByPropertyValue.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());*/

        List<Map.Entry<Object, Long>> resMap = simulationExecution.getHistogramByEntityAndValue(entityName, propertyName);
        if(resMap.size() == 0){
            text = "All " + entityName + " entities are dead, No histogram";
        }
        else {
            for (Map.Entry<Object, Long> entry : resMap) {
                text += entry.getValue() + ": " + entry.getKey() + "\n";
            }
        }
        entitiesText.setText(text);
    }

    public void clearTxt(){
        entitiesText.setText("");
    }
}

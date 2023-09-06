package subcomponents.tabs.results.histogram.population;

import dto.Dto;
import engine.simulation.Simulation;
import engine.simulation.execution.SimulationExecution;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PopulationHistogramComponentController {

    @FXML
    private TextArea populationText;

    public void presentHistogramByEntityAndValue(SimulationExecution simulationExecution, String entityName, String propertyName){
        String text = "";
        Map<Object, Long> countByPropertyValue = simulationExecution.getWorld().getInstanceManager().getInstancesByName(entityName).stream()
                .collect(Collectors.groupingBy(
                        instance -> instance.getPropertyInstanceByName(propertyName).getVal(),
                        Collectors.counting()));

        List<Map.Entry<Object, Long>> sortedEntries = countByPropertyValue.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());


        for (Map.Entry<Object, Long> entry : sortedEntries) {
            text += entry.getValue() + ": " + entry.getKey() + "\n";
        }
        populationText.setText(text);
    }


}

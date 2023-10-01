package subcomponents.tabs.results.logic.task;

import engine.simulation.Simulation;
import engine.simulation.execution.Status;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import subcomponents.tabs.results.logic.task.population.FillPopulation;

import java.util.Map;

public class TaskSimulationStop implements Runnable {
    private int simulationId;
    private Simulation simulation;
    private VBox simulationDetails;
    private TableView<FillPopulation> entityPopulation;
    private Map<String, SimpleIntegerProperty> propertyMap;


    public TaskSimulationStop(int simulationId, Simulation simulation, VBox simulationDetails, TableView<FillPopulation> entityPopulation, Map<String, SimpleIntegerProperty> propertyMap) {
        this.simulationId = simulationId;
        this.simulation = simulation;
        this.simulationDetails = simulationDetails;
        this.entityPopulation = entityPopulation;
        this.propertyMap = propertyMap;

    }

    @Override
    public void run() {
        System.out.println("TaskSimulationStop START");
        // check if paused -
        // set status to INPROGRESS
        // Resume func -> set object notifyAll + set PauseBool to false

        if (simulation.getSimulationById(simulationId).getSimulationStatus() == Status.PAUSE ||
                simulation.getSimulationById(simulationId).getSimulationStatus() == Status.IN_PROGRESS) {

            // set simulation terminated boolean to Stop
            simulation.stopSimulationById(simulationId);
            Platform.runLater(() -> {
                Label temp = ((Label) ((HBox) simulationDetails.getChildren().get(simulationId - 1))
                        .getChildren().get(1));
                temp.setText(" Stop");
                temp.setStyle("-fx-text-fill:#b90505");

            });
        }
        System.out.println("TaskSimulationStop END");
    }

}


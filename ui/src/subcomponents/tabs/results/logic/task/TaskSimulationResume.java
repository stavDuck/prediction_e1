package subcomponents.tabs.results.logic.task;

import engine.simulation.Simulation;
import engine.simulation.execution.Status;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import subcomponents.tabs.results.logic.task.population.FillPopulation;

import java.util.Map;

public class TaskSimulationResume implements Runnable{
    private int simulationId;
    private Simulation simulation;
    private SimpleLongProperty propertyCurrTick;
    private VBox simulationDetails;
    private SimpleLongProperty runningTime;
    private TableView<FillPopulation> entityPopulation;
    private Map<String, SimpleIntegerProperty> propertyMap;
    private SimpleStringProperty errorStopSimulation;


    public TaskSimulationResume(int simulationId, Simulation simulation, SimpleLongProperty propertyCurrTick,
                                SimpleLongProperty runningTime,VBox simulationDetails, TableView<FillPopulation> entityPopulation, Map<String,
            SimpleIntegerProperty> propertyMap, SimpleStringProperty errorStopSimulation) {
        this.simulationId = simulationId;
        this.simulation = simulation;
        this.propertyCurrTick = propertyCurrTick;
        this.runningTime = runningTime;
        this.simulationDetails = simulationDetails;
        this.entityPopulation = entityPopulation;
        this.propertyMap = propertyMap;
        this.errorStopSimulation = errorStopSimulation;
    }

    @Override
    public void run() {
        System.out.println("TaskSimulationResume START");

        if(simulation.getSimulationById(simulationId).getSimulationStatus() == Status.PAUSE) {

            simulation.resumeSimulationById(simulationId);
            // create new Thread
            Platform.runLater(() -> {
                Label temp = ((Label) ((HBox) simulationDetails.getChildren().get(simulationId - 1))
                        .getChildren().get(1));
                temp.setText(" Process");
                temp.setStyle("-fx-text-fill:#000000");
            });

            // create new thread for continue updates
            new Thread(() -> {
                TaskSimulationRunningDetails task = new TaskSimulationRunningDetails(simulationId, simulation, propertyCurrTick, runningTime,
                        simulationDetails, entityPopulation, propertyMap, errorStopSimulation);
                task.runTask();
            }).start();
        }

        System.out.println("TaskSimulationResume END");

    }
}

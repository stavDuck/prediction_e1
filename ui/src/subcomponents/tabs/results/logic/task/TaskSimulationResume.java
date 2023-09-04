package subcomponents.tabs.results.logic.task;

import engine.simulation.Simulation;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TaskSimulationResume implements Runnable{
    private int simulationId;
    private Simulation simulation;
    private VBox simulationDetails;
    private SimpleLongProperty propertyCurrTick;


    public TaskSimulationResume(int simulationId, Simulation simulation, SimpleLongProperty propertyCurrTick, VBox simulationDetails) {
        this.simulationId = simulationId;
        this.simulation = simulation;
        this.propertyCurrTick =propertyCurrTick;
        this.simulationDetails = simulationDetails;
    }

    @Override
    public void run() {
        System.out.println("TaskSimulationResume START");

        simulation.resumeSimulationById(simulationId);
        // create new Thread
        Platform.runLater(() -> {
            ((Label) ((HBox)simulationDetails.getChildren().get(simulationId-1))
                    .getChildren().get(1)).setText(" Process");
        });

        // create new thread for continue updates
        new Thread(()->{
            TaskSimulationRunningDetails task = new TaskSimulationRunningDetails(simulationId,simulation, propertyCurrTick, simulationDetails);
            task.runTask();
        }).start();

        System.out.println("TaskSimulationResume END");

    }
}

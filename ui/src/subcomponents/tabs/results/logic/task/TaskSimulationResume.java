package subcomponents.tabs.results.logic.task;

import engine.simulation.Simulation;
import engine.simulation.execution.Status;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TaskSimulationResume implements Runnable{
    private int simulationId;
    private Simulation simulation;
    private SimpleLongProperty propertyCurrTick;
    private VBox simulationDetails;
    private Timeline timeline;


    public TaskSimulationResume(int simulationId, Simulation simulation, SimpleLongProperty propertyCurrTick,
                                Timeline timeline,VBox simulationDetails) {
        this.simulationId = simulationId;
        this.simulation = simulation;
        this.propertyCurrTick = propertyCurrTick;
        this.timeline = timeline;
        this.simulationDetails = simulationDetails;
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
                TaskSimulationRunningDetails task = new TaskSimulationRunningDetails(simulationId, simulation, propertyCurrTick, timeline, simulationDetails);
                task.runTask();
            }).start();
        }

        System.out.println("TaskSimulationResume END");

    }
}

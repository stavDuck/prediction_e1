package subcomponents.tabs.results.logic.task;

import engine.simulation.Simulation;
import engine.simulation.execution.Status;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TaskSimulationStop implements Runnable {
    private int simulationId;
    private Simulation simulation;
    private VBox simulationDetails;

    public TaskSimulationStop(int simulationId, Simulation simulation, VBox simulationDetails) {
        this.simulationId = simulationId;
        this.simulation = simulation;
        this.simulationDetails = simulationDetails;
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


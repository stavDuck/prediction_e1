package subcomponents.tabs.results.logic.task;

import engine.simulation.Simulation;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TaskSimulationPause implements Runnable{
    private int simulationId;
    private Simulation simulation;
    private VBox simulationDetails;


    public TaskSimulationPause(int simulationId, Simulation simulation, VBox simulationDetails) {
        this.simulationId = simulationId;
        this.simulation = simulation;
        this.simulationDetails = simulationDetails;
    }

    @Override
    public void run() {
        System.out.println("TaskSimulationPause START");
        // use boolean Pause -> set true
        simulation.pauseSimulationById(simulationId);
        Platform.runLater(() -> {
            ((Label) ((HBox)simulationDetails.getChildren().get(simulationId-1))
                    .getChildren().get(1)).setText(" Pause");
        });

        System.out.println("TaskSimulationPause END");
    }
}

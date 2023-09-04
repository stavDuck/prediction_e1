package subcomponents.tabs.results.logic.task;

import dto.Dto;
import engine.simulation.Simulation;
import engine.simulation.execution.SimulationExecution;
import engine.simulation.execution.Status;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;

public class TaskSimulationRunningDetails {
    private int simulationId;
    private Simulation simulation;
    private SimpleLongProperty propertyCurrTick;

    public TaskSimulationRunningDetails(int simulationId, Simulation simulation, SimpleLongProperty propertyCurrTick) {
        this.simulationId = simulationId;
        this.simulation = simulation;
        this.propertyCurrTick = propertyCurrTick;
    }

    public void runTask(){
        // Do while + change to funcition that return DTO in Simulation by ID
        //SimulationExecution currSimulation = simulation.getSimulationById(simulationId);
        // get status
        do{
            get status;
            get dto;
            run platform;
        }
        while (currSimulation.getSimulationStatus()!= Status.FINISH &&  currSimulation.getSimulationStatus() != Status.PAUSE)


        while(currSimulation.getSimulationStatus()!= Status.FINISH &&  currSimulation.getSimulationStatus() != Status.PAUSE){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // Handle the InterruptedException if needed
            }

            // function in Simulation.getDtoDetailByID
            Dto dto = currSimulation.getWorld().createDto();
            Platform.runLater(() -> {
                propertyCurrTick.set(dto.getCurrTicks());
            });


        }
    }

}

package engine.simulation;


import dto.Dto;
import engine.simulation.execution.SimulationExecution;
import engine.simulation.execution.manager.SimultionExecutionManager;

public class Simulation {
    private SimultionExecutionManager simulationManager;

    public Simulation() {
        this.simulationManager = new SimultionExecutionManager();
    }

    public int createSimulation(String fileName) throws RuntimeException{
      int simulationId =  simulationManager.addSimulationExecution(fileName);
      return simulationId;
    }

    public SimulationExecution getSimulationById(int id){
        return simulationManager.getSimulationById(id);
    }
    public void execute(int id){
        simulationManager.execute(id);
    }
    public void pauseSimulationById(int id){
        simulationManager.pauseById(id);
    }
    public void resumeSimulationById(int id){
        simulationManager.resumeById(id);
    }
    public void stopSimulationById(int id){
        simulationManager.stopById(id);
    }
}

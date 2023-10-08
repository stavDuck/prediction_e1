package engine.simulation;


import dto.Dto;
import engine.simulation.execution.SimulationExecution;
import engine.simulation.execution.manager.SimultionExecutionManager;

import java.io.InputStream;

public class Simulation {
    private SimultionExecutionManager simulationManager;

    public Simulation() {
        this.simulationManager = new SimultionExecutionManager();
    }

    // NEW !!!
    public void createSimulation(InputStream inputStream, int id) throws RuntimeException{
        //simulationManager.addSimulationExecution(inputStream, id);
        //return simulationId;
        createSimulation(inputStream, id, "");
    }
    public void createSimulation(InputStream inputStream, int id, String userName) throws RuntimeException{
        simulationManager.addSimulationExecution(inputStream, id, userName);
        //return simulationId;
    }


    /*public int createSimulation(String fileName) throws RuntimeException{
      int simulationId =  simulationManager.addSimulationExecution(fileName);
      return simulationId;
    }*/

    public SimulationExecution getSimulationById(int id){
        return simulationManager.getSimulationById(id);
    }
    /*public void execute(int id){
        simulationManager.execute(id);
    }*/
    public void pauseSimulationById(int id){
        simulationManager.pauseById(id);
    }
    public void resumeSimulationById(int id){
        simulationManager.resumeById(id);
    }
    public void stopSimulationById(int id){
        simulationManager.stopById(id);
    }
    /*public int getRunningThreadsNumber(){
        return simulationManager.getRunningThreadsNumber();
    }
    public int getWaitingTreadsNumber(){
        return simulationManager.getWaitingTreadsNumber();
    }
    public int getCompletedThreadsNumber(){
        return simulationManager.getCompletedThreadsNumber();
    }*/

   /* public void resetUniqID(){
        simulationManager.resetUniqId();
    }*/
}

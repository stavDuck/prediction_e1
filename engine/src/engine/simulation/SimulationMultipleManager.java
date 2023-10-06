package engine.simulation;
import engine.simulation.execution.Status;
import engine.simulation.execution.runner.SingleSimulationRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

// This class will be handaling multiple xmls, every xml will be under a simulation object
// simulations will be saved accodring to the id of the simulation
public class SimulationMultipleManager {
        private Map<String, Simulation> simulationsManager;
        private ExecutorService executorService;


    public SimulationMultipleManager() {
        this.simulationsManager = new HashMap<>();

        // set default number of threads for start
        executorService = Executors.newFixedThreadPool(3);
    }

    public Map<String, Simulation> getSimulationsManager() {
        return simulationsManager;
    }

    public void setSimulationsManager(Map<String, Simulation> simulationsManager) {
        this.simulationsManager = simulationsManager;
    }

    public void addSimulationToSimulationMultipleManager(String name,Simulation simulation){
        simulationsManager.put(name, simulation);
    }

    // return true if uniq name already exist, else return false
    public boolean isNameExistInMap(String name){
        return (simulationsManager.get(name) != null);
    }

    public void removeSimulationFromSimulationMultipleManager(String name){
        simulationsManager.remove(name);
    }

    public void execute(String xmlName, int id){
        if(simulationsManager.get(xmlName).getSimulationById(id) != null &&
                simulationsManager.get(xmlName).getSimulationById(id).getSimulationStatus() == Status.CREATED) {
            SingleSimulationRunner runSimulation = new SingleSimulationRunner(simulationsManager.get(xmlName).getSimulationById(id));
            simulationsManager.get(xmlName).getSimulationById(id).setSimulationStatus(Status.IN_PROGRESS);
            executorService.execute(runSimulation);
        }
    }

    public int getRunningThreadsNumber(){
        return ((ThreadPoolExecutor)executorService).getActiveCount();
    }
    public int getWaitingTreadsNumber(){
        return (int)((ThreadPoolExecutor) executorService).getQueue().size();
    }
    public int getCompletedThreadsNumber(){
        return (int)((ThreadPoolExecutor)executorService).getCompletedTaskCount();
    }

    // function will shout down the old thread pool and create new one with the new thread number
    public void updateThreadsNumber(int newNumber){
        // Shut down the ExecutorService gracefully
        executorService.shutdown();
        executorService = Executors.newFixedThreadPool(newNumber);
    }
}

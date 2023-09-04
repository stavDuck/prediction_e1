package engine.simulation.execution.manager;
import engine.simulation.execution.SimulationExecution;
import engine.simulation.execution.Status;
import engine.simulation.execution.runner.SingleSimulationRunner;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimultionExecutionManager {
    private static int idGenerator = 1; // generate id for every new simulation
    private Map<Integer, SimulationExecution> simulations;
    private ExecutorService executorService;
    private int currRunningThreads;
    private boolean isManagerMapEmpty;

    public SimultionExecutionManager() {
        simulations = new HashMap<>();
       // executorService = Executors.newFixedThreadPool(numberOfThreads);
        currRunningThreads = 0;
        isManagerMapEmpty = true;
    }

    // create and preper simulation
    public int addSimulationExecution(String fileName) throws RuntimeException{
       SimulationExecution newSimulation = new SimulationExecution(fileName);
        newSimulation.setId(idGenerator);
        idGenerator++;

        if(isManagerMapEmpty){
            // set thread pool
            isManagerMapEmpty = false;
            executorService = Executors.newFixedThreadPool(newSimulation.getWorld().getThreadCount());
        }

        simulations.put(newSimulation.getId(), newSimulation);
        return newSimulation.getId();
    }
    public SimulationExecution getSimulationById(int id){
        return simulations.get(id);
    }

    // run created simulation
    public void execute(int id){
        if(simulations.get(id) != null && simulations.get(id).getSimulationStatus() == Status.CREATED) {
            SingleSimulationRunner runSimulation = new SingleSimulationRunner(simulations.get(id));
            simulations.get(id).setSimulationStatus(Status.IN_PROGRESS);
            executorService.execute(runSimulation);
        }
    }

    public void pauseById(int id) {
        if(simulations.get(id) != null && simulations.get(id).getSimulationStatus() == Status.IN_PROGRESS){
            simulations.get(id).pauseSimulation();
        }
    }
    public void resumeById(int id){
        simulations.get(id).resume();
    }
    public void stopById(int id) {
        simulations.get(id).stopSimulation();
    }

}

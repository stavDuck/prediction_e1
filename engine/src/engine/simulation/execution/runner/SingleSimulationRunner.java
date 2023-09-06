package engine.simulation.execution.runner;
import engine.simulation.execution.SimulationExecution;
import engine.simulation.execution.Status;

public class SingleSimulationRunner implements Runnable{
    private SimulationExecution currSimulation;

    public SingleSimulationRunner(SimulationExecution currSimulation) {
        this.currSimulation = currSimulation;
    }

    @Override
    public void run() {
        currSimulation.run();
        if(currSimulation.getSimulationStatus() != Status.STOP) {
            currSimulation.setSimulationStatus(Status.FINISH);
        }
    }
}

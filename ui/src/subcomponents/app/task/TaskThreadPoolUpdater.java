package subcomponents.app.task;

import engine.simulation.Simulation;
import engine.simulation.execution.Status;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;

public class TaskThreadPoolUpdater implements Runnable{
    private boolean isKeepUpdateThreadPoolInfo;
    private final Simulation simulation;
    private final SimpleLongProperty propertyWaitingThreadPool;
    private final SimpleLongProperty propertyRunningThreadPool;
    private final SimpleLongProperty propertyCompletedThreadPool;

    public TaskThreadPoolUpdater(boolean isKeepUpdateThreadPoolInfo, Simulation simulation,
                                 SimpleLongProperty propertyWaitingThreadPool,SimpleLongProperty propertyRunningThreadPool,SimpleLongProperty propertyCompletedThreadPool) {
        this.isKeepUpdateThreadPoolInfo = isKeepUpdateThreadPoolInfo;
        this.simulation = simulation;
        this.propertyWaitingThreadPool = propertyWaitingThreadPool;
        this.propertyRunningThreadPool = propertyRunningThreadPool;
        this.propertyCompletedThreadPool = propertyCompletedThreadPool;
    }

    public void setKeepUpdateThreadPoolInfo(boolean keepUpdateThreadPoolInfo) {
        isKeepUpdateThreadPoolInfo = keepUpdateThreadPoolInfo;
    }

    @Override
    public void run() {
        do{
            int waitingThreads = simulation.getWaitingTreadsNumber();
            int runningThreads = simulation.getRunningThreadsNumber();
            int completedThreads =simulation.getCompletedThreadsNumber();

            Platform.runLater(() -> {
                propertyWaitingThreadPool.set(waitingThreads);
                propertyRunningThreadPool.set(runningThreads);
                propertyCompletedThreadPool.set(completedThreads);
            });

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                // Handle the InterruptedException if needed
            }
        }
        while (isKeepUpdateThreadPoolInfo);
    }

}

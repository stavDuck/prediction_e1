package user.subcomponents.app.task;

import engine.simulation.Simulation;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import user.subcomponents.app.StopTaskObject;

public class TaskThreadPoolUpdater implements Runnable{
    private final Simulation simulation;
    private final SimpleLongProperty propertyWaitingThreadPool;
    private final SimpleLongProperty propertyRunningThreadPool;
    private final SimpleLongProperty propertyCompletedThreadPool;
    private StopTaskObject stopThread;

    public TaskThreadPoolUpdater(Simulation simulation, SimpleLongProperty propertyWaitingThreadPool,SimpleLongProperty propertyRunningThreadPool,
                                 SimpleLongProperty propertyCompletedThreadPool, StopTaskObject stopThread ) {
        this.simulation = simulation;
        this.propertyWaitingThreadPool = propertyWaitingThreadPool;
        this.propertyRunningThreadPool = propertyRunningThreadPool;
        this.propertyCompletedThreadPool = propertyCompletedThreadPool;
        this.stopThread = stopThread;
    }

    @Override
    public void run() {
        System.out.println("TaskThreadPoolUpdater START");
        do{
            int waitingThreads = simulation.getWaitingTreadsNumber();
            int runningThreads = simulation.getRunningThreadsNumber();
            int completedThreads =simulation.getCompletedThreadsNumber();

            Platform.runLater(() -> {
                propertyWaitingThreadPool.set(waitingThreads);
                propertyRunningThreadPool.set(runningThreads);
                propertyCompletedThreadPool.set(completedThreads);
            });

          //  System.out.println(stopThread.isStop());

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // Handle the InterruptedException if needed
            }
        }
        while(stopThread.isStop());

        System.out.println("TaskThreadPoolUpdater END");
    }
}

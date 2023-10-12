package dto.threadpool;

public class DtoThreadPool {
    private int waitingThreads;
    private int runningThreads;
    private int completedThreads;

    public DtoThreadPool(int waitingThreads, int runningThreads, int completedThreads) {
        this.waitingThreads = waitingThreads;
        this.runningThreads = runningThreads;
        this.completedThreads = completedThreads;
    }

    public int getWaitingThreads() {
        return waitingThreads;
    }
    public void setWaitingThreads(int waitingThreads) {
        this.waitingThreads = waitingThreads;
    }
    public int getRunningThreads() {
        return runningThreads;
    }
    public void setRunningThreads(int runningThreads) {
        this.runningThreads = runningThreads;
    }
    public int getCompletedThreads() {
        return completedThreads;
    }
    public void setCompletedThreads(int completedThreads) {
        this.completedThreads = completedThreads;
    }
}

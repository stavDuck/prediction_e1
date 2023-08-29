package engine.termination;

public class Termination {
    private boolean isStop = false;
    private boolean stoppedByUser;
    private Integer bySec;
    private Integer byTick;

    public Termination(int bySec, int byTick) {
        this.bySec = bySec;
        this.byTick = byTick;
        this.stoppedByUser = false;
    }
    public Termination(Integer bySec, Integer byTick) {
        this.bySec = bySec;
        this.byTick = byTick;
        this.stoppedByUser = false;
    }

    public Termination() {
        this(0,0);
    }

    public boolean isStop() {
        return isStop;
    }

    public Integer getBySec() {
        return bySec;
    }

    public Integer getByTick() {
        return byTick;
    }
    public void setStop(boolean stop) {
        isStop = stop;
    }

}

package engine.termination;

public class Termination {
    private boolean isStop = false;
    private Integer bySec;
    private Integer byTick;

    public Termination(int bySec, int byTick) {
        this.bySec = bySec;
        this.byTick = byTick;
    }
    public Termination(Integer bySec, Integer byTick) {
        this.bySec = bySec;
        this.byTick = byTick;
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

    public void printTermination() {
        System.out.println("Termination by seconds: "+ bySec);
        System.out.println("Termination by ticks: "+ byTick);

    }
    public void setStop(boolean stop) {
        isStop = stop;
    }

}

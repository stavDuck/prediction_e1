package engine.termination;

public class Termination {
    private boolean isStop = false;
    private int bySec;
    private int byTick;

    public Termination(int bySec, int byTick) {
        this.bySec = bySec;
        this.byTick = byTick;
    }

    public Termination() {
        this(0,0);
    }

    public boolean isStop() {
        return isStop;
    }

    public int getBySec() {
        return bySec;
    }

    public int getByTick() {
        return byTick;
    }

    public void printTermination() {
        System.out.println("Termination by seconds: "+ bySec);
        System.out.println("Termination by ticks: "+ byTick);

    }
}

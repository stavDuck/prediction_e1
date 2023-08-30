package engine.property.tickhistory;

public class TickHistory {
    private int startTick;
    private int endTick;

    public TickHistory() {
        this.startTick = 0;
        this.endTick = 0;
    }

    public TickHistory(int startTick, int endTick) {
        this.startTick = startTick;
        this.endTick = endTick;
    }

    public int getStartTick() {
        return startTick;
    }

    public void setStartTick(int startTick) {
        this.startTick = startTick;
    }

    public int getEndTick() {
        return endTick;
    }

    public void setEndTick(int endTick) {
        this.endTick = endTick;
    }
}

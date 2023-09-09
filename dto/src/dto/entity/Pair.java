package dto.entity;

public class Pair {
    private int tickNum;
    private int popValue;

    public Pair(int tickNum, int popValue) {
        this.tickNum = tickNum;
        this.popValue = popValue;
    }

    public int getTickNum() {
        return tickNum;
    }

    public void setTickNum(int tickNum) {
        this.tickNum = tickNum;
    }

    public int getPopValue() {
        return popValue;
    }

    public void setPopValue(int popValue) {
        this.popValue = popValue;
    }
}

package engine.range;

public class Range {
    private int to = 0;
    private int from = 0;

    public Range() {
    }

    public Range(int to, int from) {
        this.to = to;
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}

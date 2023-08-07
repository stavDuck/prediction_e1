package engine.range;

public class Range {
    private float to = 0;
    private float from = 0;

    public Range() {
    }

    public Range(float to, float from) {
        this.to = to;
        this.from = from;
    }

    public void setTo(float to) {
        this.to = to;
    }

    public void setFrom(float from) {
        this.from = from;
    }

    public float getFrom() {
        return from;
    }

    public float getTo() {
        return to;
    }
}

package engine.range;
import engine.property.type.Type;

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

    public void printRange(Type type){
        if(type.name().toLowerCase().equals("decimal")) {
            System.out.println("Range: to:" + (int) to + " from: " + (int) from);
        }
        else
            System.out.println("Range: to:" + to + " from: " + from);
    }
}

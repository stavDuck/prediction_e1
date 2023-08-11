package engine.activation;

public class Activation {
    private int tick;
    private double probability;

    public Activation(int tick, double probability) {
        this.tick = tick;
        this.probability = probability;
    }

    // getters
    public int getTick() {
        return tick;
    }
    public double getProbability() {
        return probability;
    }

    // setters
    public void setTick(int tick) {
        this.tick = tick;
    }
    public void setProbability(double probability) {
        this.probability = probability;
    }
}

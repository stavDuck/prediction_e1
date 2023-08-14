package dto.rule.activation;

public class DtoActivation {
    private int ticks;
    private double probability;

    public DtoActivation(int ticks, double probability) {
        this.ticks = ticks;
        this.probability = probability;
    }

    public void printActivation() {
        System.out.println("Activation tick: " + ticks);
        System.out.println("Activation probability: " + probability);
    }

}

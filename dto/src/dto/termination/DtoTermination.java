package dto.termination;

public class DtoTermination {
    private Integer byTick;
    private Integer bySeconds;

    public DtoTermination(Integer byTick, Integer bySeconds) {
        this.byTick = byTick;
        this.bySeconds = bySeconds;
    }

    public void printTermination() {
        System.out.println("Termination by seconds: "+ bySeconds);
        System.out.println("Termination by ticks: "+ byTick);
        System.out.println();
    }
}

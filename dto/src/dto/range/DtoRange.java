package dto.range;

public class DtoRange {
    private float to;
    private float from;

    public DtoRange(float to, float from) {
        this.to = to;
        this.from = from;
    }

    public void printRange(String type){
        if(type.toLowerCase().equals("decimal")) {
            System.out.println("Range: to: " + (int) to + " from: " + (int) from);
        }
        else
            System.out.println("Range: to: " + to + " from: " + from);

    }

}

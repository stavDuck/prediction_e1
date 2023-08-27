package dto.property;

import dto.range.DtoRange;

public class DtoProperty {
    private String name;
    private String type;
    private DtoRange range;
    private boolean isInitRandom;

    public DtoProperty(String name, String type, DtoRange range, boolean isInitRandom) {
        this.name = name;
        this.type = type;
        this.range = range;
        this.isInitRandom = isInitRandom;
    }

    public void printPropertyStructure(){
        printProperty();
        System.out.println("Is random: " + isInitRandom);
    }

    public void printProperty(){
        System.out.println("Property name: " + name);
        System.out.println("Property type: " + type.toLowerCase());
        if(range != null){
            range.printRange(type);
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public DtoRange getRange() {
        return range;
    }

    public boolean isInitRandom() {
        return isInitRandom;
    }
}

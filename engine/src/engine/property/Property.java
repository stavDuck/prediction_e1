package engine.property;

import engine.range.Range;

public class Property {
    private String name;
    private  String type;
    private Object val;
    private Range range;


    // constractor for random = false with default value
    public Property(String name, String type, Range range, String initVal) {
        this.name = name;
        this.type = type;
        this.range = range;
        //this.val = createInstance(getclassName)((convert to type)initVal)
    }
    // constractor for random = true, need to random a value in the range
    public Property(String name, String type, Range range) {
        this.name = name;
        this.type = type;
        this.range = range;
        // NEED TO RANDOM and set into val data member!!!
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public RandomInitialize getRandom() {
        return random;
    }

    public void setRandom(RandomInitialize random) {
        this.random = random;
    }
}

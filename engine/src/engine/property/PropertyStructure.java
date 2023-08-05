package engine.property;

import engine.range.Range;

public class PropertyStructure extends Property{
    boolean isRandom;
    String defaultValue;

    public PropertyStructure(String name, String strType, Range range, boolean isRandom, String defaultValue) {
        super(name, strType, range);
        this.isRandom = isRandom;
        this.defaultValue = defaultValue;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public void setRandom(boolean random) {
        isRandom = random;
    }
}

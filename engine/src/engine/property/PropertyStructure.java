package engine.property;

import engine.property.type.Type;
import engine.range.Range;

public class PropertyStructure extends Property{
    boolean isRandom;
    String defaultValue;

    public PropertyStructure(String name, String strType, Range range, boolean isRandom, String defaultValue) {
        super(name, strType, range);
        this.isRandom = isRandom;
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
    public boolean getIsRandom() {
        return isRandom;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    public void setRandom(boolean random) {
        isRandom = random;
    }

    public void printPropertyStructure(){
        super.printProperty();
       System.out.println("Is random: " + isRandom);
       if(!isRandom){
           System.out.println("Defualt value: " + defaultValue);
       }

    }


}

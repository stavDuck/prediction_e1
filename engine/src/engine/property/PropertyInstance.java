package engine.property;

import engine.range.Range;

public class PropertyInstance extends Property {
    private Object val;

    public PropertyInstance(String name, String strType, Range range, String value) {
        super(name, strType, range);
        setVal(value);
    }

    public void setVal(String value) {
       try {
           switch (type) {
               case DECIMAL:
                   val = Integer.parseInt(value);
                   break;
               case FLOAT:
                   val = Float.parseFloat(value);
                   break;
               case BOOLEAN:
                   val = Boolean.parseBoolean(value); //maybe need to add exception
                   break;
               case STRING:
                   val = value;
           }
       }
       catch (NumberFormatException e) {
           System.out.println("Property: " + name + ", has a value of: " + value + ", is not a proper number");
       }
    }
}

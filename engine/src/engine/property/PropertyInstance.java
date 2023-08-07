package engine.property;

import engine.property.type.Type;
import engine.range.Range;

public class PropertyInstance extends Property {
    private Object val;

    // doing create new instance for new entity
    public PropertyInstance(String name, Type type, Range range, Object value) {
        super(name, type, range);
        this.val = value;
    }

    // doing create new env property
    public PropertyInstance(String name, String type, Range range, Object value) {
        super(name, type, range);
        this.val = value;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object value) {
        this.val = value;
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

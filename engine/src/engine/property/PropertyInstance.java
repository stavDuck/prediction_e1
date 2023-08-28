package engine.property;
import engine.property.type.Type;
import engine.range.Range;

public class PropertyInstance extends Property {
    private Object val;
    private boolean isUserSetValue;

    // doing create new instance for new entity
    public PropertyInstance(String name, Type type, Range range, Object value) {
        super(name, type, range);
        this.val = value;
        isUserSetValue = false;
    }

    // doing create new env property
    public PropertyInstance(String name, String type, Range range, Object value) {
        super(name, type, range);
        this.val = value;
        isUserSetValue = false;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object value) {
        this.val = value;
    }
    public void setVal(String value) throws NumberFormatException {
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
           throw new NumberFormatException();
       }
    }

    public void printVal(Type type){
        switch (type) {
            case DECIMAL:
               System.out.println("Property value: " + (Integer) val);
                break;
            case FLOAT:
                System.out.println("Property value: " + (Float) val);
                break;
            case BOOLEAN:
                System.out.println("Property value: " + (Boolean) val);
                break;
            case STRING:
                System.out.println("Property value: " + (String) val);
        }
    }

    public boolean isUserSetValue() {
        return isUserSetValue;
    }

    public void setUserSetValue(boolean userSetValue) {
        isUserSetValue = userSetValue;
    }
}

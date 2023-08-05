package engine.property;

import engine.property.type.Type;
import engine.range.Range;

public class Property {

    private static final String DECIMAL = "decimal";
    private static final String FLOAT = "float";
    private static final String BOOLEAN = "boolean";
    private static final String STRING = "string";

    protected String name;
    protected Type type;
    protected Range range;


    // constractor for random = false with default value
    public Property(String name, String strType, Range range) {
        this.name = name;
        setType(strType);
        this.range = range;
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

    public void setType(Type type) {
        this.type = type;
    }

    public void setType(String type) {
        switch (type) {
            case DECIMAL:
                this.type = Type.DECIMAL;
                break;
            case FLOAT:
                this.type = Type.FLOAT;
                break;
            case BOOLEAN:
                this.type = Type.BOOLEAN;
                break;
            case STRING:
                this.type = Type.STRING;
                break;

        }
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

}

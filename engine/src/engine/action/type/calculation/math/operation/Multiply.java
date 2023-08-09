package engine.action.type.calculation.math.operation;

import engine.action.type.calculation.Calculation;
import engine.property.Property;

public class Multiply extends Calculation {
    private Property resultProp;
    private String arg1;
    private String arg2;

    public Multiply(String entityName, String actionType) {
        super(entityName, actionType);
    }
}

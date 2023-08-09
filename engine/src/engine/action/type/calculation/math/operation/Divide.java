package engine.action.type.calculation.math.operation;

import engine.action.type.calculation.Calculation;
import engine.property.Property;

public class Divide extends Calculation {
    private Property resultProp;
    private String numerator;
    private String denominator;

    public Divide(String entityName, String actionType) {
        super(entityName, actionType);
    }
}

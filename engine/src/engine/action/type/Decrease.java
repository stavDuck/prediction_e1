package engine.action.type;

import engine.action.Action;
import engine.expression.Expression;
import engine.property.Property;

public class Decrease extends Action {
    private float by;

    public Decrease(String entityName, String actionType, float by) {
        super(entityName, actionType);
        this.by = by;
    }

    //function execute
}

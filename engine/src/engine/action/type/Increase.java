package engine.action.type;

import engine.action.Action;
import engine.expression.Expression;
import engine.property.Property;

public class Increase extends Action {

    private float by;

    public Increase(String entityName, String actionType, float by) {
        super(entityName, actionType);
        this.by = by;
    }

    //function execute
}

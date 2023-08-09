package engine.action.type.calculation;

import engine.action.AbstractAction;
import engine.action.Action;
import engine.execution.context.Context;
import engine.property.Property;

public class Calculation extends AbstractAction {


    public Calculation(String entityName, String actionType) {
        super(entityName, actionType);
    }

    @Override
    public void invoke(Context context) {

    }
}

package engine.action.type;

import engine.action.AbstractAction;
import engine.execution.context.Context;

public class Condition extends AbstractAction {
    public Condition(String entityName, String actionType) {
        super(entityName, actionType);
    }

    @Override
    public void invoke(Context context) {

    }
}

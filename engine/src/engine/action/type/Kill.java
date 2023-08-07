package engine.action.type;

import engine.action.AbstractAction;
import engine.execution.context.Context;

public class Kill extends AbstractAction {

    public Kill(String entityName, String actionType){
        super(entityName,actionType);
    }
    @Override
    public void invoke(Context context) {
        context.removeEntity(context.getPrimaryEntityInstance());
    }
}

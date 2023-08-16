package engine.action.type;

import engine.action.AbstractAction;
import engine.execution.context.Context;

public class KillAction extends AbstractAction {

    public KillAction(String entityName, String actionType){
        super(entityName,actionType);
    }
    @Override
    public void invoke(Context context) {
        context.getPrimaryEntityInstance().setShouldKill(true);
        //context.removeEntity(context.getPrimaryEntityInstance());
    }
}

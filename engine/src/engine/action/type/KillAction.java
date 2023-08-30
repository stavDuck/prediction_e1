package engine.action.type;

import engine.action.AbstractAction;
import engine.action.type.condition.Condition;
import engine.execution.context.Context;

public class KillAction extends AbstractAction {

    public KillAction(String entityName, String actionType){
        super(entityName,actionType);
    }
    // ctor for secondary
    public KillAction(String entityName, String actionType, int secondaryAmount, String secondaryEntityName, Condition condition, boolean isSelectedAll){
        super(entityName,actionType, secondaryAmount, secondaryEntityName, condition, isSelectedAll);
    }

    @Override
    public void invoke(Context context) {
        context.getPrimaryEntityInstance().setShouldKill(true);
    }
}

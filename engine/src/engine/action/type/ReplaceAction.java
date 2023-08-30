package engine.action.type;

import engine.action.AbstractAction;
import engine.action.type.condition.Condition;
import engine.execution.context.Context;

public class ReplaceAction extends AbstractAction {
    private final String createEntity;
    private final String mode;

    public ReplaceAction(String killEntity, String actionType, String createEntity, String mode) {
        //in this action, kill is the main entity that's in the abstractAction class
        super(killEntity, actionType);
        this.createEntity = createEntity;
        this.mode = mode;
    }
    // ctor for secondary action
    public ReplaceAction(String killEntity, String actionType, String createEntity, String mode, int secondaryAmount, String secondaryEntityName, Condition condition, boolean isSelectedAll) {
        //in this action, kill is the main entity that's in the abstractAction class
        super(killEntity, actionType, secondaryAmount, secondaryEntityName, condition, isSelectedAll);
        this.createEntity = createEntity;
        this.mode = mode;
    }
    @Override
    public void invoke(Context context) {

    }
}

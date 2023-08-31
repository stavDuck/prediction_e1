package engine.action.type;

import engine.action.AbstractAction;
import engine.action.type.condition.Condition;
import engine.execution.context.Context;

public class ProximityAction extends AbstractAction {
    private String targetEntity;
    private String envDepthOf;

    public ProximityAction(String sourceEntity, String actionType, String targetEntity, String envDepthOf) {
        //in this action, kill is the main entity that's in the abstractAction class
        super(sourceEntity, actionType);
        this.targetEntity = targetEntity;
        this.envDepthOf = envDepthOf;
    }

    // ctor for secondary entity
    public ProximityAction(String sourceEntity, String actionType, String targetEntity, String envDepthOf, int secondaryAmount, String secondaryEntityName, Condition condition, boolean isSelectedAll) {
        //in this action, kill is the main entity that's in the abstractAction class
        super(sourceEntity, actionType, secondaryAmount, secondaryEntityName, condition, isSelectedAll);
        this.targetEntity = targetEntity;
        this.envDepthOf = envDepthOf;
    }
    @Override
    public void invoke(Context context) {

    }
}

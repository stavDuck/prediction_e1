package engine.action.type;

import engine.action.AbstractAction;
import engine.execution.context.Context;

public class ProximityAction extends AbstractAction {
    private final String targetEntity;
    private final String envDepthOf;

    public ProximityAction(String sourceEntity, String actionType, String targetEntity, String envDepthOf) {
        //in this action, kill is the main entity that's in the abstractAction class
        super(sourceEntity, actionType);
        this.targetEntity = targetEntity;
        this.envDepthOf = envDepthOf;
    }

    @Override
    public void invoke(Context context) {

    }
}

package engine.action;

import engine.entity.EntityStructure;
import engine.execution.context.Context;
public interface Action {
    void invoke(Context context);
    ActionType getActionType();
    //EntityStructure getContextEntity();
}

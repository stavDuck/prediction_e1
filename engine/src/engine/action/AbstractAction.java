package engine.action;

import engine.execution.context.Context;
import engine.property.type.Type;

public abstract class AbstractAction implements Action {
    private static final String INCREASE = "increase";
    private static final String DECREASE = "decrease";
    private static final String CALCULATION = "calculation";
    private static final String CONDITION = "condition";
    private static final String SET = "set";
    private static final String KILL = "kill";

    protected String entityName;
    protected ActionType actionType;

    public AbstractAction(String entityName, String actionType) {
        this.entityName = entityName;
        setActionType(actionType);
    }
    public AbstractAction(String entityName, ActionType actionType) {
        this.entityName = entityName;
        this.actionType = actionType;
    }

    // getters
    public String getEntityName() {
        return entityName;
    }
    @Override
    public ActionType getActionType() {
        return actionType;
    }

    // setters
    public void setActionType(String actionType) {
        switch (actionType) {
            case INCREASE:
                this.actionType = ActionType.INCREASE;
                break;
            case DECREASE:
                this.actionType = ActionType.DECREASE;
                break;
            case CONDITION:
                this.actionType = ActionType.CONDITION;
                break;
            case CALCULATION:
                this.actionType = ActionType.CALCULATION;
                break;
            case KILL:
                this.actionType = ActionType.KILL;
                break;
            case SET:
                this.actionType = ActionType.SET;
                break;


        }
    }
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void printAction() {
        System.out.println("Action Type: " + actionType.name().toLowerCase());
    }
}

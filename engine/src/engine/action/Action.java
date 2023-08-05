package engine.action;

import engine.action.type.*;
import engine.action.type.calculation.Calculation;

public class Action {
    private enum ActionType {
        INCREASE, DECREASE, CALCULATION, CONDITION, SET, KILL;
    }

    private static final String INCREASE = "increase";
    private static final String DECREASE = "decrease";
    private static final String CALCULATION = "calculation";
    private static final String CONDITION = "condition";
    private static final String SET = "set";
    private static final String KILL = "kill";

    private String entityName;
    private ActionType actionType;

    //private ActionExecute actionExecute;

    public Action(String entityName, String actionType) {
        this.entityName = entityName;
        setActionType(actionType);
    }

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
}

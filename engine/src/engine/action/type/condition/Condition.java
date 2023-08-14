package engine.action.type.condition;

import engine.action.AbstractAction;
import engine.action.type.DecreaseAction;
import engine.action.type.IncreaseAction;
import engine.action.type.KillAction;
import engine.action.type.SetAction;
import engine.action.type.calculation.Calculation;
import engine.execution.context.Context;
import java.util.ArrayList;
import java.util.List;

public class Condition extends AbstractAction {
        private conditionSingularityApi whenCondition;
        private List<AbstractAction> thenCondition; // if result from when is true
        private List<AbstractAction> elseCondition; // if result from when is false

    // when condition is created in the XML copy stage with all the information
    // and the correct type (single/multi)
    public Condition(String entityName, String actionType, conditionSingularityApi whenCondition) {
        super(entityName, actionType);
        this.whenCondition = whenCondition;
        thenCondition = new ArrayList<>();
        elseCondition = new ArrayList<>();
    }

    @Override
    public void invoke(Context context) {
       boolean check = invokeWhenCondition(context);

       // if check == true -> run thenCondition
       if(check){
           invokeAbstractActions(context, thenCondition);
       }
       else if(elseCondition.size() != 0){
           invokeAbstractActions(context, elseCondition);
       }
    }
    public boolean invokeWhenCondition(Context context){
        switch (whenCondition.getSingularity()){
            case "multi":
                ((ConditionMultiple)whenCondition).invoke(context);
                break;
            case "single":
                ((ConditionSingle)whenCondition).invoke(context);
                break;
        }
        return whenCondition.getResult();
    }
    public void invokeAbstractActions(Context context, List<AbstractAction> lst){
        for(AbstractAction action : lst){
            switch (action.getActionType()){
                case INCREASE:
                    ((IncreaseAction)action).invoke(context);
                    break;
                case DECREASE:
                    ((DecreaseAction)action).invoke(context);
                    break;
                case CALCULATION:
                    ((Calculation)action).invoke(context);
                    break;
                case CONDITION:
                    ((Condition)action).invoke(context);
                    break;
                case SET:
                    ((SetAction)action).invoke(context);
                    break;
                case KILL:
                    ((KillAction)action).invoke(context);
                    break;
            }
        }
    }

    // action is created in the XML copy side with all the information
    // and with the spesific type (increase/decrease..)
    public void addNewThenAction(AbstractAction action){
        thenCondition.add(action);
    }
    public void addNewElseAction(AbstractAction action){
        elseCondition.add(action);
    }

    // getters

    public conditionSingularityApi getWhenCondition() {
        return whenCondition;
    }

    public List<AbstractAction> getThenCondition() {
        return thenCondition;
    }

    public List<AbstractAction> getElseCondition() {
        return elseCondition;
    }
}

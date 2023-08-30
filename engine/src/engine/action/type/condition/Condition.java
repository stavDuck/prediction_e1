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

    // ctor for secondary
    public Condition(String entityName, String actionType, conditionSingularityApi whenCondition, int secondaryAmount, String secondaryEntityName, Condition condition, boolean isSelectedAll) {
        super(entityName, actionType, secondaryAmount, secondaryEntityName, condition, isSelectedAll);
        this.whenCondition = whenCondition;
        thenCondition = new ArrayList<>();
        elseCondition = new ArrayList<>();
    }
    @Override
    public void invoke(Context context) {
        // init results for rule condition
        initWhenCondition(whenCondition);

       boolean check = invokeWhenCondition(context);
       // if check == true -> run thenCondition
       if(check){
           invokeAbstractActions(context, thenCondition);
       }
       else if(elseCondition.size() != 0){
           invokeAbstractActions(context, elseCondition);
       }
    }

    // go over all condition multi set result according to logical filed
    public void initWhenCondition(conditionSingularityApi whenCondition){
        if(whenCondition.getSingularity().equals("multi")){
            // logical = and -> set true as start value
            if(((ConditionMultiple)whenCondition).getLogical().equals("and")) {
                ((ConditionMultiple) whenCondition).setResult(true);
            }
            // logical = or -> set false as start value
            else{
                ((ConditionMultiple) whenCondition).setResult(false);
            }

            // go over only multi cild in multi parent and set value according to ligical
            for(conditionSingularityApi currMultiChild : ((ConditionMultiple)whenCondition).getConditionLst()){
               if(currMultiChild.getSingularity().equals("multi")) {
                   initWhenCondition(currMultiChild);
               }
            }
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
                    //context.getPrimaryEntityInstance().setShouldKill(true);
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

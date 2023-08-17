package engine.action.type.condition;

import engine.action.AbstractAction;
import engine.action.ActionType;
import engine.execution.context.Context;

import java.util.ArrayList;
import java.util.List;

public class ConditionMultiple extends AbstractAction implements conditionSingularityApi {
   // methods
    private String logical; //will get or/and value
    private List<conditionSingularityApi> conditionLst;
    private boolean result;

    public ConditionMultiple(String entityName, String logical, String actionType) {
        super(entityName, actionType);
        this.logical = logical;
        this.conditionLst = new ArrayList<>(); // empty list

        // set result default value - if and -> start with true, if or -> start with false
        this.result  = logical.equals("and") ? true : false;
    }
    public ConditionMultiple(String entityName, String logical, ActionType actionType) {
        super(entityName, actionType);
        this.logical = logical;
        this.conditionLst = new ArrayList<>(); // empty list

        // set result default value - if and -> start with true, if or -> start with false
        this.result  = logical.equals("and") ? true : false;
    }

    // add new multi condition to the conditions list
    public void addNewMultipleCondition(String logical){
        conditionLst.add(new ConditionMultiple(super.entityName, logical, super.actionType));
    }
    //add new single condition to the conditions list
    public void addNewSingleCondition(String entityToEnvoke, String propertyToInvoke, String operator, String value){
        conditionLst.add(new ConditionSingle(super.entityName, super.actionType, entityToEnvoke,
                propertyToInvoke, operator, value));
    }

    // getters
    public String getLogical() {
        return logical;
    }
    public List<conditionSingularityApi> getConditionLst() {
        return conditionLst;
    }

    @Override
    public String getSingularity() {
        return "multi";
    }

    @Override
    public boolean getResult() {
        return result;
    }

    // setters
    public void setLogical(String logical) {
        this.logical = logical;
    }
    public void setConditionLst(List<conditionSingularityApi> conditionLst) {
        this.conditionLst = conditionLst;
    }

    @Override
    public void invoke(Context context) {
        for(conditionSingularityApi condition : conditionLst){
            switch (condition.getSingularity()){
                case "multi":
                    ((ConditionMultiple)condition).invoke(context);
                    break;
                case "single":
                    ((ConditionSingle)condition).invoke(context);
                    break;
            }
            if(logical.equals("and")){
               result =  result && condition.getResult();
            }
            else if(logical.equals("or")){
                result =  result || condition.getResult();
            }
        }
    }
}

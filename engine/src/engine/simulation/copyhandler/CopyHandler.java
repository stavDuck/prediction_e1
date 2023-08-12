package engine.simulation.copyhandler;
import engine.action.AbstractAction;
import engine.action.type.DecreaseAction;
import engine.action.type.IncreaseAction;
import engine.action.type.KillAction;
import engine.action.type.SetAction;
import engine.action.type.calculation.Calculation;
import engine.action.type.condition.Condition;
import engine.action.type.condition.ConditionMultiple;
import engine.action.type.condition.ConditionSingle;
import engine.action.type.condition.conditionSingularityApi;
import engine.activation.Activation;
import engine.entity.EntityStructure;
import engine.range.Range;
import engine.rule.Rule;
import engine.termination.Termination;
import engine.world.World;
import generated.*;

import java.util.List;



public class CopyHandler {
    private static final String INCREASE = "increase";
    private static final String DECREASE = "decrease";
    private static final String CALCULATION = "calculation";
    private static final String CONDITION = "condition";
    private static final String SET = "set";
    private static final String KILL = "kill";

    public void copyData(PRDWorld prdWorld, World world) {
        copyEnvironmentProperties(prdWorld, world);
        copyEntityStructure(prdWorld, world);
        //copy rules
        copyTermination(prdWorld, world);

    }

    public void copyEnvironmentProperties(PRDWorld prdWorld, World world) {
        List<PRDEnvProperty> prdList = prdWorld.getPRDEvironment().getPRDEnvProperty();
        for(PRDEnvProperty property : prdList) {

            // if range in null (when type is not decimal/ float) set range in our world as null
            world.getEnvironment().addEnvProperty(property.getPRDName(),
                    property.getType(),
                    (property.getPRDRange() == null ? null :
                    new Range((int)property.getPRDRange().getTo(), (int)property.getPRDRange().getFrom())),
                    null);
        }
    }

    public void copyEntityStructure(PRDWorld prdWorld, World world) {
        List<PRDEntity> prdEntityListList = prdWorld.getPRDEntities().getPRDEntity();
        for(PRDEntity entity : prdEntityListList) {
            //adding a new entity to the world's entityStructure map
            world.addEntityStructure(entity.getName(), new EntityStructure(entity.getPRDPopulation(), entity.getName()));

            List<PRDProperty> prdPropertyList = entity.getPRDProperties().getPRDProperty();
            for(PRDProperty property : prdPropertyList) {
                //go to the created entity, and add all property structures
                world.getEntityStructures().get(entity.getName()).addProperty(property.getPRDName(),
                        property.getType(),
                        new Range((int)property.getPRDRange().getTo(), (int)property.getPRDRange().getFrom()),
                        property.getPRDValue().isRandomInitialize(),
                        property.getPRDValue().getInit());
            }
        }
    }

    public void copyRules(PRDWorld prdWorld, World world) {
        // create rules in world
        // create every rule -> rule name, List of actions, activation
        // create action by type with all need info

        for (PRDRule rule : prdWorld.getPRDRules().getPRDRule()) {
            Rule newRule = copyRuleFromPRDWorld(rule);
            world.addRule(newRule.getName(), newRule);

        }

    }

     private Rule copyRuleFromPRDWorld(PRDRule rule){
        Activation newActivation = new Activation(rule.getPRDActivation().getTicks(), rule.getPRDActivation().getProbability());
        Rule newRule = new Rule(rule.getName(), newActivation);

        // adding all the actions into the list
        for(PRDAction action : rule.getPRDActions().getPRDAction()){
          AbstractAction newAction = copyActionFromPRDRule(action);
            newRule.getActions().add(newAction);
        }

        return newRule;
     }

     private AbstractAction copyActionFromPRDRule(PRDAction action){
         switch (action.getType()){
             case (DECREASE): return createNewDecrease(action);
             case (INCREASE): return createNewIncrease(action);
             case (CALCULATION): return createNewCalculation(action);
             case (CONDITION): return createNewCondition(action);
             case (SET): return createNewSet(action);
             case (KILL): return createNewKill(action);
             default: return null;
         }
     }

     public DecreaseAction createNewDecrease(PRDAction action){
        return new DecreaseAction(action.getEntity(), action.getProperty(), action.getType(), action.getBy());
     }
    public IncreaseAction createNewIncrease(PRDAction action){
        return new IncreaseAction(action.getEntity(), action.getProperty(), action.getType(), action.getBy());
    }
    public Calculation createNewCalculation(PRDAction action){
        if(action.getPRDDivide() != null && action.getPRDMultiply() == null){
            return new Calculation(action.getEntity(), action.getType(), "divide",
                    action.getResultProp(), action.getPRDDivide().getArg1(), action.getPRDDivide().getArg2());
        }
        else if(action.getPRDDivide() == null && action.getPRDMultiply() != null){
            return new Calculation(action.getEntity(), action.getType(), "multiply",
                    action.getResultProp(), action.getPRDMultiply().getArg1(), action.getPRDMultiply().getArg2());
        }
        else{
            return null; // THROW ERROR !!!
        }
    }
    public Condition createNewCondition(PRDAction action){
        // create new condition when (single or multi)
        conditionSingularityApi newWhenCondition = createNewWhen(action.getPRDCondition(), action.getEntity(), action.getType());

        Condition newCondition = new Condition(action.getEntity(), action.getType(), newWhenCondition);

        // create new then
        createNewActionInThen(newCondition,action.getPRDThen().getPRDAction());
        // create new when
        createNewActionInElse(newCondition, action.getPRDElse().getPRDAction());

        return newCondition;
    }

    // getting the PRDcondition when and creating world when condition
    private conditionSingularityApi createNewWhen(PRDCondition condition, String entityName, String actionType){
        String singularity = condition.getSingularity();
        conditionSingularityApi newWhenCondition;

        switch (singularity){
            case "multiple":
                newWhenCondition = createNewConditionMultiple(condition, entityName, actionType);
                break;
            case "single":
                // Check if list is empty
                newWhenCondition = createNewConditionSingle(condition, entityName, actionType);
                break;
            default:
                newWhenCondition = null;
                break;
        }

        return newWhenCondition;
    }

    private ConditionMultiple createNewConditionMultiple(PRDCondition condition, String entityName, String actionType){
        ConditionMultiple newConditionMultiple = new ConditionMultiple(entityName, condition.getLogical(), actionType);
        conditionSingularityApi newConditionToAdd;

        // if not null else ERROR !!!!!
        for(PRDCondition currCondition: condition.getPRDCondition()){
            switch (currCondition.getSingularity()){
                case "multiple":
                    newConditionMultiple.addNewMultipleCondition(currCondition.getLogical());
                    createNewConditionMultiple(currCondition,  entityName,  actionType);
                    break;
                case "single":
                    newConditionMultiple.addNewSingleCondition(currCondition.getEntity(), currCondition.getProperty(),
                    currCondition.getOperator(), currCondition.getValue());
                    break;
            }
        }
        return newConditionMultiple;
    }

    private ConditionSingle createNewConditionSingle(PRDCondition condition, String entityName, String actionType){

        return new ConditionSingle(entityName, actionType, condition.getEntity(),
                condition.getProperty(), condition.getOperator(), condition.getValue());
    }

    private void createNewActionInThen(Condition newCondition, List<PRDAction> prdAction ){
        for(PRDAction currAction : prdAction){
            AbstractAction newAction = copyActionFromPRDRule(currAction);
            newCondition.addNewThenAction(newAction);
        }
    }
    private void createNewActionInElse(Condition newCondition, List<PRDAction> prdAction){
        for(PRDAction currAction : prdAction){
            AbstractAction newAction = copyActionFromPRDRule(currAction);
            newCondition.addNewElseAction(newAction);
        }
    }






    public SetAction createNewSet(PRDAction action){
        return new SetAction(action.getEntity(), action.getProperty(), action.getType(), action.getValue());

    }
    public KillAction createNewKill(PRDAction action){
            return new KillAction(action.getEntity(), action.getType());
    }


    public void copyTermination(PRDWorld prdWorld, World world) {
        PRDByTicks ticks = (PRDByTicks) prdWorld.getPRDTermination().getPRDByTicksOrPRDBySecond().get(0);
        PRDBySecond seconds = (PRDBySecond) prdWorld.getPRDTermination().getPRDByTicksOrPRDBySecond().get(1);
        world.setTermination(new Termination(ticks.getCount(), seconds.getCount()));
    }
}

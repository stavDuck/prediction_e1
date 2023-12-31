package engine.simulation.copyhandler;
import engine.action.AbstractAction;
import engine.action.SecondaryInfo;
import engine.action.type.*;
import engine.action.type.calculation.Calculation;
import engine.action.type.condition.Condition;
import engine.action.type.condition.ConditionMultiple;
import engine.action.type.condition.ConditionSingle;
import engine.action.type.condition.conditionSingularityApi;
import engine.activation.Activation;
import engine.entity.EntityStructure;
import engine.grid.Grid;
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
    private static final String REPLACE = "replace";
    private static final String PROXIMITY = "proximity";
    public void copyData(PRDWorld prdWorld, World world) {
        copyEnvironmentProperties(prdWorld, world);
        copyEntityStructure(prdWorld, world);
        copyRules(prdWorld, world);
        copyTermination(prdWorld, world);
        copyGrid(prdWorld, world);
        copyTreadCount(prdWorld.getPRDThreadCount(),world);
    }

    public void copyEnvironmentProperties(PRDWorld prdWorld, World world) {
        List<PRDEnvProperty> prdList = prdWorld.getPRDEnvironment().getPRDEnvProperty();
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
            //the population is received by the user, so the initial value is 0
            world.addEntityStructure(entity.getName(), new EntityStructure(0, entity.getName()));

            List<PRDProperty> prdPropertyList = entity.getPRDProperties().getPRDProperty();
            for(PRDProperty property : prdPropertyList) {
                //go to the created entity, and add all property structures
                world.getEntityStructures().get(entity.getName()).addProperty(property.getPRDName(),
                        property.getType(),
                       (property.getPRDRange() == null) ?
                               null : new Range((int)property.getPRDRange().getTo(), (int)property.getPRDRange().getFrom()),
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
        Activation newActivation = createNewActivation(rule);
        Rule newRule = new Rule(rule.getName(), newActivation);

        // adding all the actions into the list
        for(PRDAction action : rule.getPRDActions().getPRDAction()){
          AbstractAction newAction = copyActionFromPRDRule(action);
            newRule.getActions().add(newAction);
        }
        return newRule;
     }
     private Activation createNewActivation(PRDRule rule){
         // set Activation
         Integer newTicks;
         Double probability;
         if(rule.getPRDActivation() == null){
             newTicks = 1;
             probability = 1.0;
         }
         else {
             probability = rule.getPRDActivation().getProbability() != null ? rule.getPRDActivation().getProbability() : 1;
             newTicks = rule.getPRDActivation().getTicks() != null ? rule.getPRDActivation().getTicks() : 1;
         }
         return new Activation(newTicks, probability);
     }

     private AbstractAction copyActionFromPRDRule(PRDAction action){
         AbstractAction res;

         switch (action.getType()){
             case (DECREASE):
                 res = createNewDecrease(action);
                 break;
             case (INCREASE):
                 res = createNewIncrease(action);
                 break;
             case (CALCULATION):
                 res = createNewCalculation(action);
                 break;
             case (CONDITION):
                 res = createNewCondition(action);
                 break;
             case (SET):
                 res = createNewSet(action);
                 break;
             case (KILL):
                 res = createNewKill(action);
                 break;
             case (REPLACE):
                 res = createNewReplace(action);
                 break;
             case (PROXIMITY):
                 res = createNewProximity(action);
                 break;
             default:
                 res = null;
                 break;
         }

         // create secondary if needed
         if(action.getPRDSecondaryEntity() != null){
             SecondaryInfo newSecInfo = copySecondary(action.getPRDSecondaryEntity());
             res.setSecondaryInfo(newSecInfo);
             }
         return  res;
     }

     // supporting adding secondary information to action if needed
    private SecondaryInfo copySecondary(PRDAction.PRDSecondaryEntity prdSecondaryEntity) {
        // if need to get all entities - no need of amount entities
        if (prdSecondaryEntity.getPRDSelection().getCount().equals("ALL")) {
            return new SecondaryInfo(0, prdSecondaryEntity.getEntity(), null, true);
        }

        // if not all- need to set how many and create condition if has condition
        else {
            Condition newCondition = null;
            int count = Integer.parseInt(prdSecondaryEntity.getPRDSelection().getCount());

            if (prdSecondaryEntity.getPRDSelection().getPRDCondition() != null) {
                // secondaty has only when condition- no then no else
                conditionSingularityApi newWhenCondition = createNewWhen(prdSecondaryEntity.getPRDSelection().getPRDCondition(),
                        prdSecondaryEntity.getEntity(), "condition");

                newCondition = new Condition(prdSecondaryEntity.getEntity(), "condition", newWhenCondition);
            }

            return new SecondaryInfo(count, prdSecondaryEntity.getEntity(), newCondition, false);
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
            return null;
        }
    }
    public Condition createNewCondition(PRDAction action){
        // create new condition when (single or multi)
        conditionSingularityApi newWhenCondition = createNewWhen(action.getPRDCondition(), action.getEntity(), action.getType());

        Condition newCondition = new Condition(action.getEntity(), action.getType(), newWhenCondition);

        // create new then
        createNewActionInThen(newCondition,action.getPRDThen().getPRDAction());
        // create new else - if exist
        if(action.getPRDElse() != null) {
            createNewActionInElse(newCondition, action.getPRDElse().getPRDAction());
        }

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

        for(PRDCondition currCondition: condition.getPRDCondition()){
            switch (currCondition.getSingularity()){
                case "multiple":
                    // create mew multiple to the condition list
                    newConditionMultiple.getConditionLst().add(createNewConditionMultiple(currCondition,entityName,actionType));

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

    public ReplaceAction createNewReplace(PRDAction action) { return new ReplaceAction(action.getKill(), action.getType(), action.getCreate(), action.getMode());}
    public ProximityAction createNewProximity(PRDAction action) {
        ProximityAction proximityAction =  new ProximityAction(action.getPRDBetween().getSourceEntity(), action.getType(), action.getPRDBetween().getTargetEntity(), action.getPRDEnvDepth().getOf());
        for(PRDAction currAction : action.getPRDActions().getPRDAction()) {
            proximityAction.addAction(copyActionFromPRDRule(currAction));
        }
        return proximityAction;
    }
    public void copyTermination(PRDWorld prdWorld, World world) {
        PRDByTicks ticks = null;
        PRDBySecond seconds = null;
        //or by user or tick/ seconds
        if (prdWorld.getPRDTermination().getPRDByUser() != null) {
            world.setTermination(new Termination(null, null));
        } else {
            for (Object curr : prdWorld.getPRDTermination().getPRDBySecondOrPRDByTicks()) {
                if (curr instanceof PRDByTicks) {
                    ticks = (PRDByTicks) curr;
                } else if (curr instanceof PRDBySecond) {
                    seconds = (PRDBySecond) curr;
                }
            }

        // supporting one of them is null of both has values
        world.setTermination(new Termination(seconds != null ? seconds.getCount() : null,
                ticks != null ? ticks.getCount() : null));
        }
    }

    public void copyGrid(PRDWorld prdWorld, World world) {
        world.setGrid(new Grid(prdWorld.getPRDGrid().getRows(), prdWorld.getPRDGrid().getColumns()));
    }

    public void copyTreadCount(int count, World world){
        world.setThreadCount(count);
    }
}

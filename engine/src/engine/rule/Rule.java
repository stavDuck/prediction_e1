package engine.rule;

import engine.action.AbstractAction;
import engine.action.SecondaryInfo;
import engine.activation.Activation;
import engine.entity.EntityInstance;
import engine.entity.EntityInstanceManager;
import engine.entity.EntityStructure;
import engine.environment.Environment;
import engine.execution.context.Context;
import engine.grid.Grid;

import java.util.*;

public class Rule {
    private String name;
    private List<AbstractAction> actions; //can have multiple actions in one rule
    private Activation activation;

    public Rule(String name, Activation activation) {
        this.name = name;
        actions = new ArrayList<>();
        this.activation = activation;
    }

    // getters
    public String getName() {
        return name;
    }

    public List<AbstractAction> getActions() {
        return actions;
    }

    public Activation getActivation() {
        return activation;
    }

    // setters

    public void setName(String name) {
        this.name = name;
    }

    public void setActivation(Activation activation) {
        this.activation = activation;
    }

    public void inokeRule(EntityInstanceManager instanceManager, Environment environment, EntityInstance currEntity, int currTick, Grid grid, Map<String, EntityStructure> entityStructure) {
        // run on all actions
        for (AbstractAction currAction : actions) {
            /*
            // get all instances names by stream filter
           // List<EntityInstance> lst = instanceManager.getInstancesByName(currAction.getEntityName());
           */

            // if Action is working on the correct entity, if not need to skip the action
            if (currAction.getEntityName().equals(currEntity.getEntityName())) {

                // check if action has secondery and create array of secondary
                if (currAction.getSecondaryInfo().isExistSecondary()) {
                    List<EntityInstance> secondaryInstancesLst = createSecondaryList(instanceManager, environment, currAction.getSecondaryInfo(), currTick, grid, entityStructure);

                        // invoke for every pair (curr entity, secondary entity)
                        for (EntityInstance currSecondary : secondaryInstancesLst) {
                            currAction.invoke(createContext(instanceManager, environment, currEntity, currSecondary, currTick, grid, entityStructure));
                        }

                }
                // else no secondery - invoke rule only on curr instance
                else {
                    currAction.invoke(createContext(instanceManager, environment, currEntity, currTick, grid, entityStructure));
                }


                // run on all relevant instances and incokw curr Action if valid
            /*for (EntityInstance currInstance : lst) {
                // inoke
                currAction.invoke(createContext(instanceManager, environment, currInstance));
            }*/
                // run on all instances and invoke kill if needed
           /* Iterator<EntityInstance> iterator = lst.iterator();
            while (iterator.hasNext()) {
                EntityInstance currInstance = iterator.next();
                if (currInstance.isShouldKill()) {
                    iterator.remove(); // Safely remove the current instance
                    instanceManager.killEntity(currInstance);
                }
            }*/
            }
        }
    }

    private List<EntityInstance> createSecondaryList(EntityInstanceManager instanceManager, Environment environment, SecondaryInfo secondaryInfo, int currTick, Grid grid, Map<String, EntityStructure> entityStructure) {
        int count = 0;
        List<EntityInstance> validInstancesList;
        List<EntityInstance> resLst;

        // get all valid instances
        // if has condition need to get only condition true entities
        if(secondaryInfo.getCondition() != null) {
            validInstancesList = new ArrayList<>();

            for (EntityInstance currEntity : instanceManager.getInstancesByName(secondaryInfo.getSecondaryEntityName())) {
                // create context
                Context currContext = new Context(currEntity, instanceManager, environment, currTick, grid, entityStructure);
                secondaryInfo.getCondition().invoke(currContext);

                // if condition is true on curr entity add to valid list
                if (secondaryInfo.getCondition().getWhenCondition().getResult()) {
                    validInstancesList.add(currEntity);
                }
            }
        }
        // if no condition all the entities are valid ones
        else{
            validInstancesList =  instanceManager.getInstancesByName(secondaryInfo.getSecondaryEntityName());
        }

        // set the res list
        if(secondaryInfo.getAmountEntities() > validInstancesList.size()){
            resLst = validInstancesList;
        }
        else {
            resLst = new ArrayList<>();
            Random random= new Random();
            int randIndex;
            while(count < secondaryInfo.getAmountEntities()){
                randIndex = random.nextInt(validInstancesList.size());
                resLst.add(validInstancesList.get(randIndex));
            }
        }

        return resLst;
    }


    // create context without secondary
    public Context createContext(EntityInstanceManager instanceManager, Environment environment, EntityInstance currInstance,
                                 int currTick, Grid grid, Map<String, EntityStructure> entityStructure){
        return new Context(currInstance, instanceManager, environment, currTick, grid, entityStructure);
    }
    // create context with secondary
    public Context createContext(EntityInstanceManager instanceManager, Environment environment, EntityInstance currInstance, EntityInstance secondaryInstance,
                                 int currTick, Grid grid, Map<String, EntityStructure> entityStructure){
        return new Context(currInstance, instanceManager, environment, secondaryInstance, currTick, grid, entityStructure);
    }

    public int getActionsSize() {
        return actions.size();
    }
}

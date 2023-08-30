package engine.rule;

import engine.action.AbstractAction;
import engine.action.SecondaryInfo;
import engine.activation.Activation;
import engine.entity.EntityInstance;
import engine.entity.EntityInstanceManager;
import engine.environment.Environment;
import engine.execution.context.Context;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public void inokeRule(EntityInstanceManager instanceManager, Environment environment, EntityInstance currEntity) {
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
                    List<EntityInstance> secondaryInstancesLst = createSecondaryList(instanceManager, currAction.getSecondaryInfo());

                    // invoke for every pair (curr entity, secondary entity)
                    for (EntityInstance currSecondary : secondaryInstancesLst) {
                        currAction.invoke(createContext(instanceManager, environment, currEntity, currSecondary));
                    }
                }
                // else no secondery - invoke rule only on curr instance
                else {
                    currAction.invoke(createContext(instanceManager, environment, currEntity));
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

    private List<EntityInstance> createSecondaryList(EntityInstanceManager instanceManager, SecondaryInfo secondaryInfo) {
        int count = 0;
        Iterator<EntityInstance> iterator = instanceManager.getInstancesByName(secondaryInfo.getSecondaryEntityName()).iterator();
        List<EntityInstance> newLst = new ArrayList<>();

        while (count < secondaryInfo.getAmountEntities() && iterator.hasNext()){
           // getting the current value and move to the next iterator in the list
            newLst.add(iterator.next());
        }
        return newLst;
    }


    // create context without secondary
    public Context createContext(EntityInstanceManager instanceManager, Environment environment, EntityInstance currInstance){
        return new Context(currInstance, instanceManager, environment);
    }
    // create context with secondary
    public Context createContext(EntityInstanceManager instanceManager, Environment environment, EntityInstance currInstance, EntityInstance secondaryInstance){
        return new Context(currInstance, instanceManager, environment, secondaryInstance);
    }

    public int getActionsSize() {
        return actions.size();
    }
}

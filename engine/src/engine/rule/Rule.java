package engine.rule;

import engine.action.AbstractAction;
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

    public Rule(String name, Activation activation){
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

    public void inokeRule(EntityInstanceManager instanceManager, Environment environment){
        // run on all actions
        for (AbstractAction currAction : actions) {
            // get all instances names by stream filter
            List<EntityInstance> lst = instanceManager.getInstancesByName(currAction.getEntityName());
            // run on all relevant instances and incokw curr Action if valid
            for (EntityInstance currInstance : lst) {
                // inoke
                currAction.invoke(createContext(instanceManager, environment, currInstance));
            }
            // run on all instances and invoke kill if needed
            Iterator<EntityInstance> iterator = lst.iterator();
            while (iterator.hasNext()) {
                EntityInstance currInstance = iterator.next();
                if (currInstance.isShouldKill()) {
                    iterator.remove(); // Safely remove the current instance
                    instanceManager.killEntity(currInstance);
                }
            }
        }
    }

    public Context createContext(EntityInstanceManager instanceManager, Environment environment, EntityInstance currInstance){
        return new Context(currInstance, instanceManager, environment);
    }

    public int getActionsSize() {
        return actions.size();
    }
}

package engine.execution.context;
import engine.entity.EntityInstance;
import engine.entity.EntityInstanceManager;
import engine.environment.Environment;
import engine.property.PropertyInstance;

import java.util.ArrayList;
import java.util.List;

public class Context {
    private EntityInstance primaryEntityInstance; // the instance the rule invoke onto

    private List<EntityInstance> secondaryEntityInstances;
    private EntityInstanceManager entityInstanceManager;// has a map with category and list of instanses
    private Environment env;

    public Context(EntityInstance entityInstance, EntityInstanceManager entityInstanceManager, Environment env){
        this.primaryEntityInstance = entityInstance;
        this.entityInstanceManager = entityInstanceManager;
        this.env = env;
        // init as empty list
        this.secondaryEntityInstances = new ArrayList<>();
    }
    public EntityInstance getPrimaryEntityInstance() {
        return primaryEntityInstance;
    }
    public void removeEntity(EntityInstance entityInstance) {
        entityInstanceManager.killEntity(entityInstance);
    }
    public PropertyInstance getEnvironmentVariable(String name) {
        return env.getEnvProperty(name);
    }

    public List<EntityInstance> getSecondaryEntityInstances(){
        return secondaryEntityInstances;
    }

    public EntityInstanceManager getEntityInstanceManager() {
        return entityInstanceManager;
    }

    public void addNewInstandToSecondaryEntityInstances(EntityInstance entityInstance){
        secondaryEntityInstances.add(entityInstance);
    }
}

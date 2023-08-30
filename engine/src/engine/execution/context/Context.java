package engine.execution.context;
import engine.entity.EntityInstance;
import engine.entity.EntityInstanceManager;
import engine.environment.Environment;
import engine.property.PropertyInstance;

import java.util.ArrayList;
import java.util.List;

public class Context {
    private EntityInstance primaryEntityInstance; // the instance the rule invoke onto
    private EntityInstance secondaryEntityInstance;
    private EntityInstanceManager entityInstanceManager;// has a map with category and list of instanses
    private Environment env;
    private int currTick;

    // ctor when action has secondary
    public Context(EntityInstance entityInstance, EntityInstanceManager entityInstanceManager, Environment env, EntityInstance secondaryEntityInstance, int currTick){
        this.primaryEntityInstance = entityInstance;
        this.entityInstanceManager = entityInstanceManager;
        this.env = env;
        this.secondaryEntityInstance = secondaryEntityInstance;
        this.currTick = currTick;
    }
    // ctor when action doesn't have secondary
    public Context(EntityInstance entityInstance, EntityInstanceManager entityInstanceManager, Environment env, int currTick){
        this.primaryEntityInstance = entityInstance;
        this.entityInstanceManager = entityInstanceManager;
        this.env = env;
        this.secondaryEntityInstance = null;
        this.currTick = currTick;
    }

    //getter
    public EntityInstance getPrimaryEntityInstance() {
        return primaryEntityInstance;
    }
    public void removeEntity(EntityInstance entityInstance) {
        entityInstanceManager.killEntity(entityInstance);
    }
    public PropertyInstance getEnvironmentVariable(String name) {
        return env.getEnvProperty(name);
    }

    public EntityInstance getSecondaryEntityInstance(){
        return secondaryEntityInstance;
    }

    public EntityInstanceManager getEntityInstanceManager() {
        return entityInstanceManager;
    }

    // setters
    public void setPrimaryEntityInstance(EntityInstance primaryEntityInstance) {
        this.primaryEntityInstance = primaryEntityInstance;
    }
    public void setSecondaryEntityInstance(EntityInstance secondaryEntityInstance) {
        this.secondaryEntityInstance = secondaryEntityInstance;
    }

    public int getCurrTick() {
        return currTick;
    }

    public void setCurrTick(int currTick) {
        this.currTick = currTick;
    }
}

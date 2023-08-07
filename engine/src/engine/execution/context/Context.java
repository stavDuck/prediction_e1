package engine.execution.context;

import engine.entity.EntityInstance;
import engine.entity.EntityInstanceManager;
import engine.environment.Environment;
import engine.property.PropertyInstance;

public class Context {
    private EntityInstance primaryEntityInstance; // the instance the rule invoke onto
    private EntityInstanceManager entityInstanceManager;
    private Environment env;

    public Context(EntityInstance entityInstance, EntityInstanceManager entityInstanceManager, Environment env){
        this.primaryEntityInstance = primaryEntityInstance;
        this.entityInstanceManager = entityInstanceManager;
        this.env = env;
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



}

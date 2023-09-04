package engine.execution.context;
import engine.entity.EntityInstance;
import engine.entity.EntityInstanceManager;
import engine.entity.EntityStructure;
import engine.environment.Environment;
import engine.grid.Grid;
import engine.property.PropertyInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Context {
    private EntityInstance primaryEntityInstance; // the instance the rule invoke onto
    private EntityInstance secondaryEntityInstance;
    private EntityInstanceManager entityInstanceManager;// has a map with category and list of instanses
    private Environment env;
    private int currTick;
    private Grid grid;
    private Map<String, EntityStructure> entityStructures;

    // ctor when action has secondary
    public Context(EntityInstance entityInstance, EntityInstanceManager entityInstanceManager, Environment env,
                   EntityInstance secondaryEntityInstance, int currTick, Grid grid, Map<String, EntityStructure> entityStructures){
        this.primaryEntityInstance = entityInstance;
        this.entityInstanceManager = entityInstanceManager;
        this.env = env;
        this.secondaryEntityInstance = secondaryEntityInstance;
        this.currTick = currTick;
        this.grid = grid;
        this.entityStructures = entityStructures;
    }
    // ctor when action doesn't have secondary
    public Context(EntityInstance entityInstance, EntityInstanceManager entityInstanceManager, Environment env, int currTick, Grid grid, Map<String, EntityStructure> entityStructures){
        this.primaryEntityInstance = entityInstance;
        this.entityInstanceManager = entityInstanceManager;
        this.env = env;
        this.secondaryEntityInstance = null;
        this.currTick = currTick;
        this.grid = grid;
        this.entityStructures = entityStructures;
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
    public Grid getGrid() {
        return grid;
    }
    public Map<String, EntityStructure> getEntityStructures() {
        return entityStructures;
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
    public void setGrid(Grid grid) {
        this.grid = grid;
    }
    public void setEntityStructures(Map<String, EntityStructure> entityStructures) {
        this.entityStructures = entityStructures;
    }
}

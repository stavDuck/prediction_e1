package engine.world;

import engine.entity.EntityInstance;
import engine.entity.EntityInstanceManager;
import engine.entity.EntityStructure;
import engine.environment.Environment;
import engine.rule.Rules;
import engine.termination.Termination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
    private Environment environment;

    private EntityInstanceManager instanceManager;

    //private Map<String, List<EntityInstance>> entityInstances; //key = smoker, value = list of all smoker instances
    private Map<String, EntityStructure> entityStructures; //key = smoker, value = structure
    private Termination termination;
    private Map<String, Rules> rules; //key = rule name, value = rule

    public World() {
        this.environment = new Environment();
        this.instanceManager = new EntityInstanceManager();
        this.entityStructures = new HashMap<>();
        this.termination = new Termination();
    }

    // getters
    public Environment getEnvironment() {
        return environment;
    }

    public EntityInstanceManager getInstanceManager() {
        return instanceManager;
    }
    public Map<String, EntityStructure> getEntityStructures() {
        return entityStructures;
    }
    public Map<String, Rules> getRules() {
        return rules;
    }
    public Termination getTermination() {
        return termination;
    }


    // setters
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setInstanceManager(EntityInstanceManager instanceManager) {
        this.instanceManager = instanceManager;
    }

    public void setEntityStructures(Map<String, EntityStructure> entityStructures) {
        this.entityStructures = entityStructures;
    }
    public void setRules(Map<String, Rules> rules) {
        this.rules = rules;
    }
    public void setTermination(Termination termination) {
        this.termination = termination;
    }



    public void addEntityStructure(String entityName, EntityStructure entityStructure) {
        this.entityStructures.put(entityName, entityStructure);
    }

    public void addEntityInstance(String entityName){
        instanceManager.create(entityStructures.get(entityName));
    }
}

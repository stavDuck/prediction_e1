package engine.world;

import engine.entity.EntityInstanceManager;
import engine.entity.EntityStructure;
import engine.environment.Environment;
import engine.rule.Rule;
import engine.termination.Termination;

import java.util.HashMap;
import java.util.Map;

public class World {
    private Environment environment;

    private EntityInstanceManager instanceManager;

    //private Map<String, List<EntityInstance>> entityInstances; //key = smoker, value = list of all smoker instances
    private Map<String, EntityStructure> entityStructures; //key = smoker, value = structure
    private Termination termination;
    private Map<String, Rule> rules; //key = rule name, value = rule

    public World() {
        this.environment = new Environment();
        this.instanceManager = new EntityInstanceManager();
        this.entityStructures = new HashMap<>();
        this.rules = new HashMap<>();
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
    public Map<String, Rule> getRules() {
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
    public void setRules(Map<String, Rule> rules) {
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
    public void addRule(String ruleName, Rule newRule){
        rules.put(ruleName, newRule);
    }
}

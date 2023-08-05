package engine.world;

import engine.entity.EntityInstance;
import engine.entity.EntityStructure;
import engine.environment.Environment;
import engine.rule.Rules;
import engine.termination.Termination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
    private Environment environment;
    private Map<String, List<EntityInstance>> entityInstances; //key = smoker, value = list of all smoker instances
    private Map<String, EntityStructure> entityStructures; //key = smoker, value = structure
    private Termination termination;

    public World() {
        this.environment = new Environment();
        this.entityInstances = new HashMap<>();
        this.entityStructures = new HashMap<>();
        this.termination = new Termination();
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Map<String, List<EntityInstance>> getEntityInstances() {
        return entityInstances;
    }

    public void setEntityInstances(Map<String, List<EntityInstance>> entityInstances) {
        this.entityInstances = entityInstances;
    }

    public Map<String, EntityStructure> getEntityStructures() {
        return entityStructures;
    }

    public void setEntityStructures(Map<String, EntityStructure> entityStructures) {
        this.entityStructures = entityStructures;
    }

    public Map<String, Rules> getRules() {
        return rules;
    }

    public void setRules(Map<String, Rules> rules) {
        this.rules = rules;
    }

    public Termination getTermination() {
        return termination;
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    private Map<String, Rules> rules; //key = rule name, value = rule


    public void addEntityStructure(String entityName, EntityStructure entityStructure) {
        this.entityStructures.put(entityName, entityStructure);
    }
}

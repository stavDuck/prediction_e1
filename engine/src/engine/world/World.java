package engine.world;

import dto.Dto;
import engine.action.AbstractAction;
import engine.entity.EntityInstanceManager;
import engine.entity.EntityStructure;
import engine.environment.Environment;
import engine.property.PropertyInstance;
import engine.property.PropertyStructure;
import engine.rule.Rule;
import engine.termination.Termination;
import engine.value.generator.ValueGeneratorFactory;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class World {
    private Environment environment;
    private EntityInstanceManager instanceManager;
    private Map<String, EntityStructure> entityStructures; //key = smoker, value = structure
    private Termination termination;
    private Map<String, Rule> rules; //key = rule name, value = rule

    public World() {
        this.environment = new Environment();
        this.instanceManager = new EntityInstanceManager();
        this.entityStructures = new HashMap<>();
        this.rules = new LinkedHashMap<>();
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

    public void setEnvValueByName(String name, String value) throws NumberFormatException {
        environment.setEnvProperty(name,value);
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

    public void createEntitiesInstances(){
        for(EntityStructure currStructure : entityStructures.values()){
            IntStream.range(0, currStructure.getPopulation())
                    .forEach(i -> instanceManager.create(currStructure));
        }
    }

    public void invokeRules() {
        int tick = 0;
        // save the start time in seconds
        long startTimeSeconds = System.currentTimeMillis() / 1000;

        while (!termination.isStop()){
            // run on the rules
            for(Rule currRule : rules.values()){
                // if rule is active
                if(isRuleActive(currRule, tick)) {
                    currRule.inokeRule(instanceManager, environment);
                }
            }
            tick ++;
            // check if termination coditions are met
            isSimulationTerminated(tick, startTimeSeconds);
        }
    }

    public void isSimulationTerminated(int tick, long startTimeSeconds){
        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        if((termination.getByTick() != null) && termination.getByTick() == tick) {
            termination.setStop(true);
            System.out.println("Simulation is Over !!!, you reached the max Ticks");
        }
        else if((termination.getBySec() != null) && (currentTimeSeconds - startTimeSeconds) >= termination.getBySec()){
            termination.setStop(true);
            System.out.println("Simulation is Over !!! , you reached the max seconds");
        }
    }

    public boolean isRuleActive(Rule currRule, int tick){
        int ruleTick = currRule.getActivation().getTick();
        double ruleProb = currRule.getActivation().getProbability();
        double randProb =(double) (ValueGeneratorFactory.createRandomFloat(0.0f,1.0f).generateValue());

        if(tick % ruleTick == 0){ // e.g. if rul tick is 2 -> will run in tick: 2/4/6/8...
            // check if rule is active now
           if((ruleProb != 0) && (ruleProb == 1 || randProb < ruleProb))
               return true;
        }
        return false;
    }

    public Dto createDto() {
        Dto dto = new Dto();
        for(EntityStructure entityStructure : entityStructures.values()) {
            dto.addEntity(entityStructure.getEntityName(), entityStructure.getPopulation());
            for(PropertyStructure propertyStructure : entityStructure.getEntityPropMap().values()) {
                if(propertyStructure.getRange() != null) {
                    dto.addPropertyToEntity(entityStructure.getEntityName(), propertyStructure.getName(),
                            propertyStructure.getType().name(), propertyStructure.getRange().getTo(), propertyStructure.getRange().getFrom(),
                            propertyStructure.getIsRandom());
                }
                else {
                    dto.addPropertyToEntity(entityStructure.getEntityName(), propertyStructure.getName(),
                            propertyStructure.getType().name(), null,
                            propertyStructure.getIsRandom());
                }
            }
        }

        for(Rule rule : rules.values()) {
            dto.addRule(rule.getName(), rule.getActivation().getTick(), rule.getActivation().getProbability(), rule.getActionsSize());
            for(AbstractAction action : rule.getActions()) {
                dto.addActionToRule(rule.getName(), action.getActionType().name());
            }
        }

        for(PropertyInstance currEnv : environment.getPropertyInstancesMap().values()) {
            if (currEnv.getRange() != null) {
                dto.addEnv(currEnv.getType().name(), currEnv.getName(),
                        currEnv.getRange().getTo(), currEnv.getRange().getFrom());
            } else {
                dto.addEnv(currEnv.getType().name(), currEnv.getName(), null);
            }
        }

        dto.addTermination(termination.getByTick(), termination.getBySec());

        return dto;
    }

    public void setPopulationForEntity(String entityName, int population) {
        entityStructures.get(entityName).setPopulation(population);
    }
}

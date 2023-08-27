package dto;

import dto.entity.DtoEntity;
import dto.env.DtoEnv;
import dto.range.DtoRange;
import dto.rule.DtoRule;
import dto.rule.activation.DtoActivation;
import dto.termination.DtoTermination;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Dto {
    private Map<String, DtoEntity> entities;
    private Map<String, DtoRule> rules;
    private DtoTermination termination;
    private Map<String, DtoEnv> envs;

    public Dto() {
        this.entities = new HashMap<>();
        this.rules = new LinkedHashMap<>();
        this.envs = new HashMap<>();
    }

    //getter
    public Map<String, DtoEntity> getEntities() {
        return entities;
    }
    public Map<String, DtoRule> getRules() {
        return rules;
    }
    public Map<String, DtoEnv> getEnvs(){return envs;}
    public DtoTermination getTermination(){return termination;}

    public void addEntity(String entityName, int entityPopulation) {
        entities.put(entityName, new DtoEntity(entityName, entityPopulation));
    }

    public void addPropertyToEntity(String entityName, String propertyName, String propertyType, float rangeTo, float rangeFrom, boolean isInitRandom) {
        DtoRange range = new DtoRange(rangeFrom, rangeTo);
        addPropertyToEntity(entityName, propertyName, propertyType, range, isInitRandom);
        //entities.get(entityName).addPropertyToEntity(propertyName, propertyType, range, isInitRandom);
    }

    public void addEnv(String envType, String envName, float to, float from){
        envs.put(envName, new DtoEnv(envType, envName, from, to));
    }

    public void addPropertyToEntity(String entityName, String propertyName, String propertyType, DtoRange range, boolean isInitRandom) {
        entities.get(entityName).addPropertyToEntity(propertyName, propertyType, range, isInitRandom);
    }

    public void addRule(String ruleName, int ticks, double probability, int actionNumber) {
        DtoActivation activation = new DtoActivation(ticks, probability);
        addRule(ruleName, activation, actionNumber);
    }
    public void addRule(String ruleName, DtoActivation activation, int actionNumber) {
        rules.put(ruleName, new DtoRule(ruleName, activation, actionNumber));
    }


    public void addActionToRule(String ruleName, String actionName) {
        rules.get(ruleName).addAction(actionName);
    }

    public void addTermination(Integer byTicks, Integer bySeconds) {
        this.termination = new DtoTermination(byTicks, bySeconds);
    }

    public void printDto() {
        printEntitiesStructure();
        printRules();
        printTermination();
    }

    public void printEntitiesStructure(){
        final int[] count = {1};
        entities.values().forEach(value -> {
            System.out.print(count[0] + ". ");
            value.printEntityStructure();
            System.out.println();
            count[0]++;
        });
    }

    public void printRules(){
        rules.values().forEach(DtoRule::printRule);
    }

    public void printTermination(){
        termination.printTermination();
    }

}


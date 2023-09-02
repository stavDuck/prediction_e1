package dto;

import dto.entity.DtoEntity;
import dto.env.DtoEnv;
import dto.grid.DtoGrid;
import dto.range.DtoRange;
import dto.rule.Action.*;
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
    private DtoGrid grid;

    public Dto() {
        this.entities = new HashMap<>();
        this.rules = new LinkedHashMap<>();
        this.envs = new HashMap<>();
    }

    //getter
    public DtoGrid getGrid() {
        return grid;
    }
    public void setGrid(DtoGrid grid) {
        this.grid = grid;
    }
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
    public void addEnv(String envType, String envName, DtoRange range){
        envs.put(envName, new DtoEnv(envType, envName, range));
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

    // add action according to the type
    public void addIncreaseAction(String ruleName,String property, String byExpression, String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity) {
        rules.get(ruleName).addAction(new DtoIncrease(property, byExpression, type, primaryEntity, isSecondaryExist, secondaryEntity));
    }
    public void addDecreaseAction(String ruleName,String property, String byExpression, String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity){
        rules.get(ruleName).addAction(new DtoDecrease(property, byExpression, type, primaryEntity, isSecondaryExist, secondaryEntity));
    }
    public void addCalculationAction(String ruleName,String operatorType, String resultProp, String arg1, String arg2,
                                     String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity){
        rules.get(ruleName).addAction(new DtoCalculation(operatorType,resultProp,arg1,arg2,type,primaryEntity,isSecondaryExist,secondaryEntity));
    }
    public void addSingelConditionAction(String ruleName,String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity, String property, String operator, String value, int thenConditionsNumber, int elseConditionsNumber){
        DtoSingleCondition singleCondition = new DtoSingleCondition( type,  primaryEntity,  isSecondaryExist,  secondaryEntity,  property,  operator,  value);
        // ctor for single condition
        rules.get(ruleName).addAction(new DtoCondition(type,  primaryEntity,  isSecondaryExist,  secondaryEntity,
                singleCondition, thenConditionsNumber, elseConditionsNumber));
    }
    public void addMultipleConditionAction(String ruleName,String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity, String logic, int conditionNumber,int thenConditionsNumber, int elseConditionsNumber){
        DtoMultipleCondition multipleCondition = new DtoMultipleCondition( type,  primaryEntity,  isSecondaryExist,  secondaryEntity,  logic,  conditionNumber);
        // ctor for multiple condition
        rules.get(ruleName).addAction(new DtoCondition(type,  primaryEntity,  isSecondaryExist,  secondaryEntity,  multipleCondition,
                thenConditionsNumber,  elseConditionsNumber));
    }
    public void addSetAction(String ruleName,String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity, String property, String newValue){
        rules.get(ruleName).addAction(new DtoSet(type,primaryEntity,isSecondaryExist,secondaryEntity,property,newValue));
    }
    public void addKillAction(String ruleName,String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity){
        rules.get(ruleName).addAction(new DtoKill(type,primaryEntity,isSecondaryExist,secondaryEntity));
    }
    public void addReplaceAction(String ruleName,String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity, String createEntity, String mode){
        rules.get(ruleName).addAction(new DtoReplace(type,primaryEntity,isSecondaryExist,secondaryEntity,createEntity,mode));
    }
    public void addProximityAction(String ruleName,String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity, String targetEntity, String envDepthOf){
        rules.get(ruleName).addAction(new DtoProximity( type,  primaryEntity,  isSecondaryExist,  secondaryEntity,  targetEntity,  envDepthOf));
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


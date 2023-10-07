package engine.world;

import com.google.gson.Gson;
import dto.Dto;
import dto.entity.Pair;
import dto.grid.DtoGrid;
import engine.Position;
import engine.action.AbstractAction;
import engine.action.type.*;
import engine.action.type.calculation.Calculation;
import engine.action.type.condition.Condition;
import engine.action.type.condition.ConditionMultiple;
import engine.action.type.condition.ConditionSingle;
import engine.entity.EntityInstance;
import engine.entity.EntityInstanceManager;
import engine.entity.EntityStructure;
import engine.environment.Environment;
import engine.grid.Grid;
import engine.property.PropertyInstance;
import engine.property.PropertyStructure;
import engine.rule.Rule;
import engine.termination.Termination;
import engine.value.generator.ValueGeneratorFactory;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class World {
    private Environment environment;
    private EntityInstanceManager instanceManager;
    private Map<String, EntityStructure> entityStructures; //key = smoker, value = structure
    private Termination termination;
    private Map<String, Rule> rules; //key = rule name, value = rule
    private Grid grid;
    private int threadCount;
    public int currTick;
    private boolean isPaused;
    private Object pauseObject;
    private long runningTime;
    private String errorStopSimulation;
private String name;
private Integer sleep;


    public World() {
        this.environment = new Environment();
        this.instanceManager = new EntityInstanceManager();
        this.entityStructures = new HashMap<>();
        this.rules = new LinkedHashMap<>();
        this.termination = new Termination();
        this.grid = new Grid();
        this.threadCount = 0;
        this.currTick = 0;
        isPaused = false;
        pauseObject = new Object();
        errorStopSimulation = "";
        this.sleep = null;
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

    public Grid getGrid() {
        return grid;
    }
    public int getGridRows(){
        return grid.getRows();
    }
    public int getGridCols(){
        return grid.getColumns();
    }
    public int getMaxEntityInstancesAmount(){
        return (getGridRows() * getGridCols());
    }
    public EntityInstance getPositionInGridBoard(int x, int y){
        return grid.getPositionInGridBoard(x, y);
    }
    public int getThreadCount() {
        return threadCount;
    }

    // setters
    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void setGridPosition(EntityInstance val, int x, int y){
        grid.setPositionInGridBoard(val, x, y);
    }

    public void setInitGrid(int rows, int cols){
        grid.setRows(rows);
        grid.setColumns(cols);
        grid.initGridBoard();
    }

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
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    public void setEnvValueByName(String name, String value) throws NumberFormatException {
        environment.setEnvProperty(name,value);
    }
    public void setEnvOriginalValueByName(String name, String value) throws NumberFormatException {
        environment.setEnvOriginalProperty(name,value);
    }


    public void addEntityStructure(String entityName, EntityStructure entityStructure) {
        this.entityStructures.put(entityName, entityStructure);
    }
    public void addEntityInstance(String entityName){
        instanceManager.create(entityStructures.get(entityName), grid);
    }
    public void addRule(String ruleName, Rule newRule){
        rules.put(ruleName, newRule);
    }

    public void createEntitiesInstances(){
        for(EntityStructure currStructure : entityStructures.values()){
            IntStream.range(0, currStructure.getPopulation())
                    .forEach(i -> instanceManager.create(currStructure, grid));
        }
    }

    public void invokeRules() throws InterruptedException {
        final int[] tick = {0};

        // get list of valid rules according to tick
        // run on all entities -> invoke all rules
            // before invoking rule check if have secondery - if true create list of secondary

        // save the start time in seconds
        long startTimeSeconds = System.currentTimeMillis() / 1000;

        while (!termination.isStop()) {
            this.runningTime = System.currentTimeMillis() / 1000 - startTimeSeconds;
            // if simulation is pause
            synchronized (pauseObject) {
                while (isPaused) {
                    try {
                        pauseObject.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            // Move all instances on the grid
            moveAllInstancesInGrid(instanceManager);

            // save the population of all entities
            for (String currEntityName : instanceManager.getAllInstances().keySet()) {
                // go over every entity and save the curr population - if the last value is the same don't add new pair
                int popHistorySize = entityStructures.get(currEntityName).getPopulationHistoryList().size();

                if(popHistorySize == 0 ||
                        entityStructures.get(currEntityName).getPopulationHistoryList().get(popHistorySize-1).getPopValue() !=
                        instanceManager.getAllInstances().get(currEntityName).size()){

                    entityStructures.get(currEntityName).addToPopulationHistoryList(
                          currTick, instanceManager.getAllInstances().get(currEntityName).size());
                }
            }


            List<Rule> ActiveRules = rules.values()
                    .stream()
                    .filter(obj -> isRuleActive(obj, tick[0])) // Replace filterFunction with your actual filter function
                    .collect(Collectors.toList());

            for (String currEntityName : instanceManager.getAllInstances().keySet()) {
                // go over all the entities from the sama category
                for (EntityInstance currEntity : instanceManager.getInstancesByName(currEntityName)) {
                    // on every entity run all the active rules
                    for (Rule currRule : ActiveRules) {
                        try {
                            currRule.inokeRule(instanceManager, environment, currEntity, tick[0], grid, entityStructures);
                        }
                        catch (RuntimeException e){
                            // set error
                            errorStopSimulation = "Stopped cause: " + e.getMessage();

                            // stop simulation
                            termination.setStop(true);
                        }
                    }
                }
            }


            tick[0]++;
            this.currTick = tick[0];
            // go over all the entities and delete the unwanted instances
            for (String currEntityName : instanceManager.getAllInstances().keySet()) {
                // go over all the entities from the sama category
                Iterator<EntityInstance> iterator = instanceManager.getInstancesByName(currEntityName).iterator();

                while (iterator.hasNext()) {
                    EntityInstance currInstance = iterator.next();
                    if (currInstance.isShouldKill()) {
                        iterator.remove(); // Safely remove the current instance
                        instanceManager.killEntity(currInstance);
                        // decrease the number in population
                        instanceManager.setCurrPopulationNumber(instanceManager.getCurrPopulationNumber() - 1);
                    }
                }
            }

            // check if termination coditions are met
            isSimulationTerminated(tick[0], startTimeSeconds);
            //Thread.sleep(1000);
        }

        // save the last value and the last tick
        for (String currEntityName : instanceManager.getAllInstances().keySet()) {
            entityStructures.get(currEntityName).addToPopulationHistoryList(
                    currTick, instanceManager.getAllInstances().get(currEntityName).size());
        }
    }

    private void moveAllInstancesInGrid(EntityInstanceManager instanceManager) {
        int increaseX;
        int decreaseX;
        int increaseY;
        int decreaseY;


        if (grid.getFreeSpots() != 0) {
            for (String currEntityName : instanceManager.getAllInstances().keySet()) {
                // go over all the entities from the sama category
                for (EntityInstance currEntity : instanceManager.getInstancesByName(currEntityName)) {
                    Position currEntityPos = currEntity.getPos();
                    // to support infinity movement in the grid
                    increaseX = (currEntityPos.getX() + 1) % grid.getRows();
                    increaseY = (currEntityPos.getY() + 1) % grid.getColumns();

                    if(currEntityPos.getX()!=0) {
                        decreaseX = (currEntityPos.getX() - 1) % grid.getRows();
                    }
                    else{
                        decreaseX = grid.getRows()-1;
                    }

                    if(currEntityPos.getY()!=0) {
                        decreaseY = (currEntityPos.getY() - 1) % grid.getColumns();
                    }
                    else{
                        decreaseY = grid.getColumns()-1;
                    }

                    // move right
                    if (grid.getPositionInGridBoard(currEntityPos.getX(), increaseY) == null) {
                        // free curr entity pos in grid
                        grid.setPositionInGridBoard(null, currEntityPos.getX(), currEntityPos.getY());

                        // set a new pos in entity and in grid
                        currEntityPos.setY(increaseY);
                        grid.setPositionInGridBoard(currEntity, currEntityPos.getX(), increaseY);
                    }

                    // move left
                    else if(grid.getPositionInGridBoard(currEntityPos.getX(), decreaseY) == null){
                        // free curr entity pos in grid
                        grid.setPositionInGridBoard(null, currEntityPos.getX(), currEntityPos.getY());

                        // set a new pos in entity and in grid
                        currEntityPos.setY(decreaseY);
                        grid.setPositionInGridBoard(currEntity, currEntityPos.getX(), decreaseY);

                    }

                    // move up
                    else if(grid.getPositionInGridBoard(decreaseX, currEntityPos.getY()) == null){
                        // free curr entity pos in grid
                        grid.setPositionInGridBoard(null, currEntityPos.getX(), currEntityPos.getY());
                        // set a new pos in entity and in grid
                        currEntityPos.setX(decreaseX);
                        grid.setPositionInGridBoard(currEntity, decreaseX, currEntityPos.getY());
                    }

                    // move down
                    else if(grid.getPositionInGridBoard(increaseX, currEntityPos.getY()) == null){
                        // free curr entity pos in grid
                        grid.setPositionInGridBoard(null, currEntityPos.getX(), currEntityPos.getY());
                        // set a new pos in entity and in grid
                        currEntityPos.setX(increaseX);
                        grid.setPositionInGridBoard(currEntity, increaseX, currEntityPos.getY());

                    }
                }
            }
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
        else if(termination.isStoppedByUser()){
            termination.setStop(true);
            System.out.println("Simulation is Over !!! , Stopped by user");
            errorStopSimulation = "Simulation is stopped by user";

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
            // create populationHistory
            List<Pair> popHistoryDto = getPopHistoryList(entityStructure.getPopulationHistoryList());

            dto.addEntity(entityStructure.getEntityName(), entityStructure.getPopulation(), popHistoryDto);
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
                // add rule action by it's type
                addActionToRuleDto(dto,rule.getName(),action);
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
        dto.setGrid(new DtoGrid(grid.getRows(), grid.getColumns()));
        dto.setCurrTicks(currTick);
        dto.setErrorStopSimulation(errorStopSimulation);

        return dto;
    }


    private List<Pair> getPopHistoryList(List<engine.entity.Pair> populationHistoryList) {
        List<Pair> res = new ArrayList<>();
        for (engine.entity.Pair currPair : populationHistoryList){
            res.add(new Pair(currPair.getTickNum(), currPair.getPopValue()));
        }
        return res;
    }

    public void addActionToRuleDto(Dto dto, String ruleName, AbstractAction action){
        // add new dto rule's action according to the action type
        switch (action.getActionType().name().toLowerCase()){
            case "increase":
                IncreaseAction tempActionIncrease = (IncreaseAction) action;
                dto.addIncreaseAction(ruleName, tempActionIncrease.getProperty(), tempActionIncrease.getByExpression(), "increase",
                        action.getEntityName(), action.getSecondaryInfo().isExistSecondary(),action.getSecondaryInfo().getSecondaryEntityName());
                break;
            case "decrease":
                DecreaseAction tempActionDecrease = (DecreaseAction) action;
                dto.addDecreaseAction(ruleName, tempActionDecrease.getProperty(),tempActionDecrease.getByExpression(), "decrease",
                        action.getEntityName(), action.getSecondaryInfo().isExistSecondary(),action.getSecondaryInfo().getSecondaryEntityName());
                break;
            case "calculation":
                Calculation tempActionCalculation = (Calculation) action;
                dto.addCalculationAction(ruleName, tempActionCalculation.getOperatorType(), tempActionCalculation.getResultProp(),
                       tempActionCalculation.getArg1(),tempActionCalculation.getArg2(), "calculation",
                        action.getEntityName(), action.getSecondaryInfo().isExistSecondary(),action.getSecondaryInfo().getSecondaryEntityName());
                break;
            case "condition":
                Condition tempActionCondition = (Condition) action;

                if(tempActionCondition.getWhenCondition().getSingularity().equals("single")){
                    ConditionSingle tempSingle = (ConditionSingle)tempActionCondition.getWhenCondition();

                    dto.addSingelConditionAction(ruleName, "condition",action.getEntityName(), action.getSecondaryInfo().isExistSecondary(),action.getSecondaryInfo().getSecondaryEntityName(),
                            tempSingle.getPropertyToInvoke(), tempSingle.getOp().name().toLowerCase(), tempSingle.getValue(),
                            tempActionCondition.getThenCondition().size(), tempActionCondition.getElseCondition().size());
                }
                else{
                    ConditionMultiple tempMulti = (ConditionMultiple)tempActionCondition.getWhenCondition();

                    dto.addMultipleConditionAction(ruleName, "condition", action.getEntityName(), action.getSecondaryInfo().isExistSecondary(),action.getSecondaryInfo().getSecondaryEntityName(),
                            tempMulti.getLogical(), tempMulti.getConditionLst().size(), tempActionCondition.getThenCondition().size(), tempActionCondition.getElseCondition().size());
                }
                break;
            case "set":
                SetAction tempActionSet = (SetAction) action;
                dto.addSetAction(ruleName, "set",action.getEntityName(),
                        action.getSecondaryInfo().isExistSecondary(),action.getSecondaryInfo().getSecondaryEntityName(),
                        tempActionSet.getProperty(),tempActionSet.getNewValue());
                break;
            case "kill":
                dto.addKillAction(ruleName, "kill", action.getEntityName(), action.getSecondaryInfo().isExistSecondary(),action.getSecondaryInfo().getSecondaryEntityName());
                break;
            case "replace":
                ReplaceAction tempActionReplace = (ReplaceAction) action;
                dto.addReplaceAction(ruleName, "replace",action.getEntityName(), action.getSecondaryInfo().isExistSecondary(),action.getSecondaryInfo().getSecondaryEntityName(),
                        tempActionReplace.getCreateEntity(), tempActionReplace.getMode());
                break;
            case "proximity":
                ProximityAction tempActionProximity = (ProximityAction) action;
                dto.addProximityAction(ruleName, "proximity", action.getEntityName(), action.getSecondaryInfo().isExistSecondary(),action.getSecondaryInfo().getSecondaryEntityName(),
                        tempActionProximity.getTargetEntity(), tempActionProximity.getEnvDepthOf());
                break;
        }
    }

    public void setPopulationForEntity(String entityName, int population) {
        entityStructures.get(entityName).setPopulation(population);
    }
    public int getPopulationForEntity(String entityName) {
        return entityStructures.get(entityName).getPopulation();
    }

    public void pause() {
        isPaused = true;
    }
    public void resume(){
        isPaused = false;
        synchronized (pauseObject) {
            pauseObject.notifyAll();
        }
    }
    public void stopByUser(){
        termination.setStoppedByUser(true);
    }

    public long getRunningTime() {
        return runningTime;
    }

    public String getErrorStopSimulation() {
        return errorStopSimulation;
    }

    public void setErrorStopSimulation(String errorStopSimulation) {
        this.errorStopSimulation = errorStopSimulation;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSleep() {
        return sleep;
    }

    public void setSleep(Integer sleep) {
        this.sleep = sleep;
    }

}

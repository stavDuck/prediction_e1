package engine.world;

import dto.Dto;
import engine.Position;
import engine.action.AbstractAction;
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

    public World() {
        this.environment = new Environment();
        this.instanceManager = new EntityInstanceManager();
        this.entityStructures = new HashMap<>();
        this.rules = new LinkedHashMap<>();
        this.termination = new Termination();
        this.grid = new Grid();
        this.threadCount = 0;
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

    public void invokeRules() {
        final int[] tick = {0};

        // get list of valid rules according to tick
        // run on all entities -> invoke all rules
            // before invoking rule check if have secondery - if true create list of secondary

        // save the start time in seconds
        long startTimeSeconds = System.currentTimeMillis() / 1000;

        while (!termination.isStop()) {
            // Move all instances on the grid
            moveAllInstancesInGrid(instanceManager);

            List<Rule> ActiveRules = rules.values()
                    .stream()
                    .filter(obj -> isRuleActive(obj, tick[0])) // Replace filterFunction with your actual filter function
                    .collect(Collectors.toList());

            for (String currEntityName : instanceManager.getAllInstances().keySet()) {
                // go over all the entities from the sama category
                for (EntityInstance currEntity : instanceManager.getInstancesByName(currEntityName)) {
                    // on every entity run all the active rules
                    for (Rule currRule : ActiveRules) {
                        currRule.inokeRule(instanceManager, environment, currEntity, tick[0], grid, entityStructures);
                    }
                }
            }


            tick[0]++;

            // go over all the entities and delete the unwanted instances
            for (String currEntityName : instanceManager.getAllInstances().keySet()) {
                // go over all the entities from the sama category
                    Iterator<EntityInstance> iterator = instanceManager.getInstancesByName(currEntityName).iterator();

                    while (iterator.hasNext()) {
                        EntityInstance currInstance = iterator.next();
                        if (currInstance.isShouldKill()) {
                            iterator.remove(); // Safely remove the current instance
                            instanceManager.killEntity(currInstance);
                        }
                    }
            }

            // check if termination coditions are met
            isSimulationTerminated(tick[0], startTimeSeconds);
        }



        // save the start time in seconds
        /*long startTimeSeconds = System.currentTimeMillis() / 1000;

        while (!termination.isStop()){
            // run on the rules
            for(Rule currRule : rules.values()){
                // if rule is active
                if(isRuleActive(currRule, tick)) {
                    currRule.inokeRule(instanceManager, environment, tick);
                }
            }
            tick++;
            // check if termination coditions are met
            isSimulationTerminated(tick, startTimeSeconds);
        }*/
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
                    decreaseX = (currEntityPos.getX() - 1) % grid.getRows();
                    increaseY = (currEntityPos.getY() + 1) % grid.getColumns();
                    decreaseY = (currEntityPos.getY() - 1) % grid.getColumns();

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

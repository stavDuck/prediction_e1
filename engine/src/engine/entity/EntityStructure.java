package engine.entity;
import engine.property.PropertyStructure;
import engine.range.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityStructure {
    // data members
    private int population;
    private String entityName;
    private Map<String, PropertyStructure> entityPropMap; //key = prop name (age), value = property
    private List<Integer> populationHistoryList;

    public EntityStructure() {
        this(0, "");
    }
    public EntityStructure(int population, String entityName) {
        this.population = population;
        this.entityName = entityName;
        entityPropMap = new HashMap<>();
        populationHistoryList = new ArrayList<>();
    }

    // getters
    public int getPopulation() {
        return population;
    }
    public String getEntityName() {
        return entityName;
    }
    public Map<String, PropertyStructure> getEntityPropMap() {
        return entityPropMap;
    }
    public List<Integer> getPopulationHistoryList() {
        return populationHistoryList;
    }

    // setters
    public void addToPopulationHistoryList(int currPop){
        populationHistoryList.add(currPop);
    }
    public void addProperty(String name, String strType, Range range, boolean isRandom, String defaultValue) {
        entityPropMap.put(name, new PropertyStructure(name, strType, range, isRandom, defaultValue));
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}

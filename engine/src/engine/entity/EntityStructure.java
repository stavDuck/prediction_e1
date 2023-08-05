package engine.entity;

import engine.property.PropertyStructure;
import engine.range.Range;

import java.util.HashMap;
import java.util.Map;

public class EntityStructure {
    private int population;
    private String entityName;
    private Map<String, PropertyStructure> entityPropMap; //key = prop name (age), value = property

    public EntityStructure() {
        this(0, "");
    }
    public EntityStructure(int population, String entityName) {
        this.population = population;
        this.entityName = entityName;
        entityPropMap = new HashMap<>();
    }

    public void addProperty(String name, String strType, Range range, boolean isRandom, String defaultValue) {
        entityPropMap.put(name, new PropertyStructure(name, strType, range, isRandom, defaultValue));
    }
}

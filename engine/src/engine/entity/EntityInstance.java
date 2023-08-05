package engine.entity;

import engine.property.PropertyInstance;
import engine.range.Range;

import java.util.HashMap;
import java.util.Map;

public class EntityInstance {
    private String entityName;
    public Map<String, PropertyInstance> propertyInstancesMap;

    public EntityInstance() {
        this("");
    }
    public EntityInstance(String entityName) {
        this.entityName = entityName;
        this.propertyInstancesMap = new HashMap<>();
    }

    public void addProperty(String name, String strType, Range range, String value) {
        propertyInstancesMap.put(name, new PropertyInstance(name, strType, range, value));
    }

}

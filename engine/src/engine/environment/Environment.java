package engine.environment;

import engine.property.PropertyInstance;
import engine.range.Range;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Map<String, PropertyInstance> propertyInstancesMap;

    public Environment() {
        propertyInstancesMap = new HashMap<>();
    }

    public void addEnvProperty(String name, String strType, Range range, String value) {
        propertyInstancesMap.put(name, new PropertyInstance(name, strType, range, value));
    }

    public void setEnvProperty(String name, String value) {
        propertyInstancesMap.get(name).setVal(value);
    }

    public PropertyInstance getEnvProperty(String name) {
        return propertyInstancesMap.get(name);
    }

}

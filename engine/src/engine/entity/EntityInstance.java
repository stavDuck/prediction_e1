package engine.entity;

import engine.property.PropertyInstance;
import engine.property.PropertyStructure;
import engine.property.type.Type;
import engine.range.Range;
import engine.value.generator.ValueGenerator;
import engine.value.generator.ValueGeneratorFactory;

import java.util.HashMap;
import java.util.Map;

public class EntityInstance {
    // data members
    private int id;
    private String entityName;
    private Map<String, PropertyInstance> propertyInstancesMap;

    public EntityInstance() {
        this("", 0);
    }

    public EntityInstance(String entityName, int id) {
        this.entityName = entityName;
        this.propertyInstancesMap = new HashMap<>();
    }

    // getters
    public Map<String, PropertyInstance> getPropertyInstancesMap() {
        return propertyInstancesMap;
    }

    public PropertyInstance getPropertyInstanceByName(String name){
        return propertyInstancesMap.get(name);
    }

    public String getEntityName() {
        return entityName;
    }

    // setters


    // Function gets entity strucher and create all the properties of the instances with all the valid values
    public void createPropertyInstancesMap(EntityStructure entityStructure){
        // go over every property in the entity template
        for(PropertyStructure propStucture: entityStructure.getEntityPropMap().values()){
            // calculate the value according to the information in the template (randomize or defualt value)
            Object val = calculateVal(propStucture.getType(),propStucture.getRange(), propStucture.getIsRandom(), propStucture.getDefaultValue());
           // add a new propetry to the instance
            addProperty(propStucture.getName(), propStucture.getType(), propStucture.getRange(), val);
        }

    }

    private Object calculateVal(Type type,Range range, boolean isRandomVal, String defVal){
        if(isRandomVal){
            switch (type){
                case DECIMAL:
                    return ValueGeneratorFactory.createRandomInteger((int)range.getFrom(), (int)range.getTo()).generateValue();
                case FLOAT:
                    return ValueGeneratorFactory.createRandomFloat(range.getFrom(),range.getTo()).generateValue();
                case BOOLEAN:
                    return ValueGeneratorFactory.createRandomBoolean().generateValue();
                default: // String type
                    return ValueGeneratorFactory.createRandomString().generateValue();
            }
        }
        // get fixed value
        else{
           return parseByTypeAndString(type,defVal);
        }
    }

    // assuming all defult values in XML are correct type
    private Object parseByTypeAndString(Type type, String defVal){
        switch (type){
            case DECIMAL:
                return Integer.parseInt(defVal);
            case FLOAT:
                return Float.parseFloat(defVal);
            case BOOLEAN:
                return Boolean.parseBoolean(defVal);
            default: // String type
                return defVal;
        }
    }
    public void addProperty(String name, Type strType, Range range, Object value) {
        propertyInstancesMap.put(name, new PropertyInstance(name, strType, range, value));
    }

}

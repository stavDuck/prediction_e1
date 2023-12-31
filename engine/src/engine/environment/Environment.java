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

   // getters
   public PropertyInstance getEnvProperty(String name) {
       return propertyInstancesMap.get(name);
   }
   public Map<String, PropertyInstance> getPropertyInstancesMap(){
        return propertyInstancesMap;
   }

   // setters
    public void setEnvProperty(String name, String value) throws NumberFormatException{
        propertyInstancesMap.get(name).setVal(value);
    }

    public void setEnvOriginalProperty(String name, String value) throws NumberFormatException {
        propertyInstancesMap.get(name).setOriginalValueFromUser(value);
    }

    public Object getEnvOriginalProperty(String name, String value) throws NumberFormatException {
        return propertyInstancesMap.get(name).getOriginalValueFromUser();
    }


    public void addEnvProperty(String name, String strType, Range range, String value) {
        propertyInstancesMap.put(name, new PropertyInstance(name, strType, range, value));
    }
}

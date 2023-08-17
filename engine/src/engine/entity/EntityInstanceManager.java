package engine.entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityInstanceManager {
    private int count;
    private Map<String, List<EntityInstance>> instances; //key = smoker, value = list of all smoker instances

    public EntityInstanceManager() {
        count = 0;
        instances = new HashMap<>();
    }

    public EntityInstance create(EntityStructure entityStructure) {
        count++;
        EntityInstance newEntityInstance = new EntityInstance(entityStructure.getEntityName(), count);
       // helper function to fill propertyInstanse map by entity structure
        newEntityInstance.createPropertyInstancesMap(entityStructure);

        // check if map don't have the entity need to create new key and value
        if(instances.get(newEntityInstance.getEntityName()) == null)
            instances.put(newEntityInstance.getEntityName(),new ArrayList<>());
        // add to the end of the list for the spesific instance
        instances.get(newEntityInstance.getEntityName()).add(newEntityInstance);
        return newEntityInstance;
    }

    public Map<String, List<EntityInstance>> getAllInstances() {
        return instances;
    }

    public List<EntityInstance> getInstancesByName(String name){
        return instances.get(name);
    }

    public void killEntity(EntityInstance entityInstance) {
        // go to the value of entity name and remove from list the recived instance in the list
        getInstancesByName(entityInstance.getEntityName()).remove(entityInstance);
    }
}

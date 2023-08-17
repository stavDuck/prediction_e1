package engine.validation.entitiesValidator;

import engine.validation.exceptions.XmlValidationException;
import generated.PRDEntity;
import generated.PRDWorld;
import java.util.List;


public class EntitiesValidator {
    //need to add exeptions
    public void validateEntitiesData(PRDWorld prdWorld) throws XmlValidationException{
        EntityValidator entityValidator = new EntityValidator();
        List<PRDEntity> prdEntityList = prdWorld.getPRDEntities().getPRDEntity();

        // go over every entity stracture in the list and validate the entity name + properties linked to it
        for(PRDEntity prdEntity : prdEntityList) {
            try {
                entityValidator.validateEntityData(prdEntity);
            }
            catch (XmlValidationException e){
                throw new XmlValidationException("Entity validation error: \n" + e.getMessage());
            }
        }
    }
}

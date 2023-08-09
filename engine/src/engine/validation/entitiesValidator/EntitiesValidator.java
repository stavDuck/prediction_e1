package engine.validation.entitiesValidator;

import generated.PRDEntity;
import generated.PRDWorld;

import java.util.List;

public class EntitiesValidator {
    //need to add exeptions
    public static boolean validateEntitiesData(PRDWorld prdWorld) {
        boolean res = false;
        List<PRDEntity> prdEntityList = prdWorld.getPRDEntities().getPRDEntity();
        for(PRDEntity prdEntity : prdEntityList) {
            res = EntityValidator.validateEntityData(prdEntity);
            if(!res)
                return false;
        }
        return res;
    }

    // helper functions
    // validate entities name is uniq
    // validate name is with no spaces
}

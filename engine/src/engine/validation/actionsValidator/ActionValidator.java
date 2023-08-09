package engine.validation.actionsValidator;

import engine.validation.ValidationCommonFunctions;
import generated.PRDAction;
import generated.PRDEntities;
import generated.PRDEntity;
import generated.PRDProperty;

public class ActionValidator extends ValidationCommonFunctions {
    public static boolean validateActionData(PRDAction prdAction, PRDEntities entities) {
        //get the entity from action
        PRDEntity prdEntity = entities.getPRDEntity().stream().
                filter(prdEntity1 -> prdAction.getEntity().isEmpty())
                .findFirst().orElse(null);
        //check if not null
        if(prdEntity == null) {
            return false;
        }

        //get the property from action
        PRDProperty prdProperty = prdEntity.getPRDProperties().getPRDProperty().stream().
                filter(prdProperty1 -> prdAction.getProperty().isEmpty()).
                findFirst().orElse(null);
        //get the entity from action
        if(prdAction.getType() != "kill" && prdProperty == null)
            return false;
        return true;

    }

}

package engine.validation.actionsValidator;

import engine.validation.ValidationCommonFunctions;
import generated.PRDAction;
import generated.PRDEntities;
import generated.PRDEvironment;
import generated.PRDRule;

import java.util.List;

public class ActionsValidator {
    public boolean validateActionsData(PRDRule prdRule, PRDEntities prdEntities, PRDEvironment prdEvironment) {
        boolean res;
        ActionValidator actionValidator = new ActionValidator();

        List<PRDAction> prdActionList = prdRule.getPRDActions().getPRDAction();
        for (PRDAction prdAction : prdActionList) {
            res = actionValidator.validateActionData(prdAction, prdEntities, prdEvironment);
            if(!res) {
                return false;
            }
        }
        return true;
    }

}

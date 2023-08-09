package engine.validation.actionsValidator;

import engine.validation.ValidationCommonFunctions;
import generated.PRDAction;
import generated.PRDEntities;
import generated.PRDEvironment;
import generated.PRDRule;

import java.util.List;

public class ActionsValidator {
    public static boolean validateActionsData(PRDRule prdRule, PRDEntities prdEntities, PRDEvironment prdEvironment) {
        boolean res;
        List<PRDAction> prdActionList = prdRule.getPRDActions().getPRDAction();
        for (PRDAction prdAction : prdActionList) {
            res = ActionValidator.validateActionData(prdAction, prdEntities, prdEvironment);
            if(!res) {
                return false;
            }
        }
        return true;
    }

}

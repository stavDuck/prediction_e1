package engine.validation.actionsValidator;

import engine.validation.ValidationCommonFunctions;
import engine.validation.exceptions.XmlValidationException;
import generated.PRDAction;
import generated.PRDEntities;
import generated.PRDEvironment;
import generated.PRDRule;

import java.util.List;

public class ActionsValidator {
    public void validateActionsData(PRDRule prdRule, PRDEntities prdEntities, PRDEvironment prdEvironment) throws XmlValidationException {
        ActionValidator actionValidator = new ActionValidator();

        List<PRDAction> prdActionList = prdRule.getPRDActions().getPRDAction();
        for (PRDAction prdAction : prdActionList) {
            actionValidator.validateActionData(prdAction, prdEntities, prdEvironment);
        }
    }

}

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
            try {
                actionValidator.validateActionData(prdAction, prdEntities, prdEvironment);
            }
            catch (XmlValidationException e) {
                throw new XmlValidationException("Rule: " + prdRule.getName() + " action: " + prdAction.getType().toLowerCase() +
                        " failed validation with the error: " + e.getMessage());
            }
        }
    }

}

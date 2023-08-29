package engine.validation.actionsValidator;
import engine.validation.exceptions.XmlValidationException;
import generated.*;

import java.util.List;

public class ActionsValidator {
    public void validateActionsData(PRDRule prdRule, PRDEntities prdEntities, PRDEnvironment prdEvironment) throws XmlValidationException {
        ActionValidator actionValidator = new ActionValidator();

        List<PRDAction> prdActionList = prdRule.getPRDActions().getPRDAction();
        for (PRDAction prdAction : prdActionList) {
            try {
                actionValidator.validateActionData(prdAction, prdEntities, prdEvironment);
            }
            catch (XmlValidationException e) {
                throw new XmlValidationException("Rule: " + prdRule.getName() + ", action: " + prdAction.getType().toLowerCase() +
                        ", failed validation with the error: \n" + e.getMessage());
            }
        }
    }

}

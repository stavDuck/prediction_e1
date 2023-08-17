package engine.validation.rulesValidator;

import engine.validation.ValidationCommonFunctions;
import engine.validation.actionsValidator.ActionsValidator;
import engine.validation.exceptions.XmlValidationException;
import generated.PRDRule;
import generated.PRDWorld;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulesValidator extends ValidationCommonFunctions {
    public void validateRulesData(PRDWorld prdWorld) throws XmlValidationException {
        ActionsValidator actionsValidator = new ActionsValidator();

        List<PRDRule> prdRuleList = prdWorld.getPRDRules().getPRDRule();
        Map<String, Integer> prdNameList = new HashMap<>(); // this map will be for validating all names are uniques

        for (PRDRule rule : prdRuleList) {
            String trimmedName = rule.getName().trim();

            //check if entity name is invalid
            if (!isNameValid(trimmedName)) {
                throw new XmlValidationException("Rule: " + trimmedName +
                        " name is not valid, name should not have spaces");
            }

            actionsValidator.validateActionsData(rule, prdWorld.getPRDEntities(), prdWorld.getPRDEvironment());
            prdNameList.put(trimmedName, (prdNameList.get(trimmedName) == null ?
                    1 : prdNameList.get(trimmedName + 1)));
        }

        // check if any name not unique
        if (!isNameUnique(prdNameList)) {
            throw new XmlValidationException("Invalid rule list, all rule names should be unique, one or more has the same name");
        }
    }
}
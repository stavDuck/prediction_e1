package engine.validation.rulesValidator;

import engine.validation.ValidationCommonFunctions;
import engine.validation.actionsValidator.ActionsValidator;
import generated.PRDRule;
import generated.PRDWorld;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulesValidator extends ValidationCommonFunctions {
    public boolean validateRulesData(PRDWorld prdWorld) {
        boolean res;
        ActionsValidator actionsValidator = new ActionsValidator();

        List<PRDRule> prdRuleList = prdWorld.getPRDRules().getPRDRule();
        Map<String, Integer> prdNameList = new HashMap<>(); // this map will be for validating all names are uniques

        for (PRDRule rule : prdRuleList) {
            String trimmedName = rule.getName().trim();

            //check if entity name is invalid
            if(!isNameValid(trimmedName)) {
                return false;
            }

            res = actionsValidator.validateActionsData(rule, prdWorld.getPRDEntities(), prdWorld.getPRDEvironment());
            if (!res)
                return false;
            prdNameList.put(trimmedName, (prdNameList.get(trimmedName) == null ?
                    1 : prdNameList.get(trimmedName + 1)));

        }
        return isNameUnique(prdNameList);
    }
}
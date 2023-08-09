package engine.validation;

import engine.validation.actionsValidator.ActionValidator;
import engine.validation.envValidator.EnvValidator;
import engine.validation.rulesValidator.RulesValidator;
import generated.PRDWorld;

public class WorldValidator {
    public static boolean validateWorldData(PRDWorld prdWorld) {
        Boolean res;
        //res = EnvValidator.validateEnvData(prdWorld);
        res = RulesValidator.validateRulesData(prdWorld);
        return res;
    }

}

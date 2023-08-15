package engine.validation;

import engine.validation.entitiesValidator.EntitiesValidator;
import engine.validation.envValidator.EnvValidator;
import engine.validation.exceptions.XmlValidationException;
import engine.validation.rulesValidator.RulesValidator;
import engine.validation.terminationValidator.TerminationValidator;
import generated.PRDWorld;

public class WorldValidator {
    public void validateWorldData(PRDWorld prdWorld) throws XmlValidationException {
        EnvValidator envValidator = new EnvValidator();
        EntitiesValidator entitiesValidator = new EntitiesValidator();
        RulesValidator rulesValidator = new RulesValidator();
        TerminationValidator terminationValidator = new TerminationValidator();

        try {
            envValidator.validateEnvData(prdWorld);
            entitiesValidator.validateEntitiesData(prdWorld);
            rulesValidator.validateRulesData(prdWorld);
            terminationValidator.validateTerminationData(prdWorld);
        }
        catch (XmlValidationException e){
            throw new XmlValidationException("XML data validation failed with the error: \n" + e.getMessage());
        }

    }

}

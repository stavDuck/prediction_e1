package engine.validation;
import engine.validation.entitiesValidator.EntitiesValidator;
import engine.validation.envValidator.EnvValidator;
import engine.validation.exceptions.XmlValidationException;
import engine.validation.gridValidator.GridValidator;
import engine.validation.rulesValidator.RulesValidator;
import engine.validation.terminationValidator.TerminationValidator;
import generated.PRDWorld;

public class WorldValidator {
    public void validateWorldData(PRDWorld prdWorld) throws XmlValidationException {
        EnvValidator envValidator = new EnvValidator();
        EntitiesValidator entitiesValidator = new EntitiesValidator();
        RulesValidator rulesValidator = new RulesValidator();
        TerminationValidator terminationValidator = new TerminationValidator();
        GridValidator gridValidator = new GridValidator();

        envValidator.validateEnvData(prdWorld);
        entitiesValidator.validateEntitiesData(prdWorld);
        rulesValidator.validateRulesData(prdWorld);
        terminationValidator.validateTerminationData(prdWorld);
        gridValidator.validateGridData(prdWorld);
    }
}

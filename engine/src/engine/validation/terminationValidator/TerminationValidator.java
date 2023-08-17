package engine.validation.terminationValidator;
import engine.validation.exceptions.XmlValidationException;
import generated.PRDBySecond;
import generated.PRDByTicks;
import generated.PRDWorld;

public class TerminationValidator {

    public boolean validateTerminationData(PRDWorld prdWorld) throws XmlValidationException {
        PRDByTicks ticks = null;
        PRDBySecond seconds = null;
        for (Object curr : prdWorld.getPRDTermination().getPRDByTicksOrPRDBySecond()){
            if(curr instanceof PRDByTicks){
                ticks = (PRDByTicks) curr;
            }
            else if(curr instanceof PRDBySecond){
                seconds = (PRDBySecond) curr;
            }
        }
        if (ticks == null && seconds == null)
            throw new XmlValidationException("Invalid Termination conditions, ticks and seconds can't be both null");

        return true;
    }
}

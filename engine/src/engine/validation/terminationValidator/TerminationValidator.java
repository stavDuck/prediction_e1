package engine.validation.terminationValidator;
import engine.validation.exceptions.XmlValidationException;
import generated.PRDBySecond;
import generated.PRDByTicks;
import generated.PRDWorld;

public class TerminationValidator {

    /*public boolean validateTerminationData(PRDWorld prdWorld) throws XmlValidationException {
        PRDByTicks ticks = null;
        PRDBySecond seconds = null;
        for (Object curr : prdWorld.getPRDTermination().getPRDBySecondOrPRDByTicks()){
            if(curr instanceof PRDByTicks){
                ticks = (PRDByTicks) curr;
            }
            else if(curr instanceof PRDBySecond){
                seconds = (PRDBySecond) curr;
            }
        }
        //if no termination
        if (ticks == null && seconds == null && prdWorld.getPRDTermination().getPRDByUser() == null)
            throw new XmlValidationException("Invalid Termination conditions, please choose a termination option");
        //if termination by user AND another option
        else if(prdWorld.getPRDTermination().getPRDByUser() != null && (ticks != null || seconds != null)) {
            throw new XmlValidationException("Invalid Termination conditions, 'User Termination' cannot be combined with other options");
        }
        return true;
    }*/
}

package engine.validation.envValidator;

import engine.world.World;
import generated.PRDEnvProperty;

public class envValidator {

    public static void validateEnvData(World world){

    }
    // validate if the name has no spaces
    public static boolean isEnvPropertyNameValid(String name) {
        if(name.contains(" "))
            return false;
        return true;
    }

    // validate range has values only if property type is decimal/float
   /* public static boolean isRangeLegal(PRDEnvProperty envProp){

    }*/



    // test if range fileds from/ to are decimal numbers
}

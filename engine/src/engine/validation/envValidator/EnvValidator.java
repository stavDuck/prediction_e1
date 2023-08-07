package engine.validation.envValidator;
import engine.validation.ValidationCommonFunctions;
import generated.PRDEnvProperty;
import generated.PRDWorld;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class EnvValidator extends ValidationCommonFunctions {

    // function go over all the property list of env prop and return if valid or not
    public static boolean validateEnvData(PRDWorld prdWorld){
        boolean res;
        List<PRDEnvProperty> prdList = prdWorld.getPRDEvironment().getPRDEnvProperty();
        Map<String, Integer> prdNameList = new HashMap<>(); // this map will be for validating all names are uniques

        // go over every property and check if valid
        for(PRDEnvProperty property : prdList) {

            if(!isNameValid(property.getPRDName()) ||
                    !isPropertyTypeValid(property.getType()) ||
                    !isRangeLegalWithDecimalOrFloat(property.getType(), property.getPRDRange()) ||
                    ((property.getPRDRange() != null) && !isRangeFiledsValid(property.getPRDRange())))
                return false;

            // add name to the map list
            prdNameList.put(property.getPRDName(), (prdNameList.get(property.getPRDName()) == null ?
                                                    1 : prdNameList.get(property.getPRDName()) + 1));
        }

        // check if any name not unique
         res = isNameUnique(prdNameList);

            return res;
    }

    // helper functions

}

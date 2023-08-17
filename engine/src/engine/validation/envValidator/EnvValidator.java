package engine.validation.envValidator;
import engine.validation.ValidationCommonFunctions;
import engine.validation.exceptions.XmlValidationException;
import generated.PRDEnvProperty;
import generated.PRDWorld;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnvValidator extends ValidationCommonFunctions {

    // function go over all the property list of env prop and return if valid or not
    public void validateEnvData(PRDWorld prdWorld) throws XmlValidationException {
        boolean res;
        List<PRDEnvProperty> prdList = prdWorld.getPRDEvironment().getPRDEnvProperty();
        Map<String, Integer> prdNameList = new HashMap<>(); // this map will be for validating all names are uniques
        String trimmedName;
        // go over every property and check if valid
        for (PRDEnvProperty property : prdList) {
            //need to check for trimmed name
            trimmedName = property.getPRDName().trim();

            if (!isNameValid(trimmedName)) {
                throw new XmlValidationException("Environment property: " + property.getPRDName() +
                        " name is not valid, name should not have spaces");
            }
            if (!isPropertyTypeValid(property.getType())) {
                throw new XmlValidationException("Environment property: " + property.getPRDName() +
                        " type is not valid, only type decimal, float, string or boolean allowed");
            }
            if (!isRangeLegalWithDecimalOrFloat(property.getType(), property.getPRDRange())) {
                throw new XmlValidationException("Environment property:: " + property.getPRDName() +
                        " is not valid, if type is not decimal or float range should not appear");
            }
            if ((property.getPRDRange() != null) && !isRangeFiledsValid(property.getPRDRange())) {
                throw new XmlValidationException("Environment property: " + property.getPRDName() +
                        " has invalid range. 'From' value should be smaller than 'To'");
            }

            // add name to the map list
            prdNameList.put(trimmedName, (prdNameList.get(trimmedName) == null ?
                    1 : prdNameList.get(trimmedName) + 1));
        }

        // check if any name not unique
        if (!isNameUnique(prdNameList)) {
            throw new XmlValidationException("Invalid environment properties list, all Environment properties need to have unique names, one or more has the same name");
        }
    }
}

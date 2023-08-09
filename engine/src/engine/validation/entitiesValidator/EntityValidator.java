package engine.validation.entitiesValidator;

import engine.validation.ValidationCommonFunctions;
import generated.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityValidator extends ValidationCommonFunctions {
    //need to add exceptions
    public static boolean validateEntityData(PRDEntity prdEntity) {
        String trimmedPropName;
        List<PRDProperty> prdList = prdEntity.getPRDProperties().getPRDProperty(); //property list of specific entity
        Map<String, Integer> prdNameList = new HashMap<>(); // this map will be for validating all names are uniques
        String trimmedName = prdEntity.getName().trim();

        //check if entity name is invalid
        if(!isNameValid(trimmedName)) {
            return false;
        }

        // go over every property and check if valid
        for(PRDProperty property : prdList) {
            trimmedPropName = property.getPRDName().trim();
            if(!isNameValid(trimmedPropName) ||
                    !isPropertyTypeValid(property.getType()) ||
                    !isRangeLegalWithDecimalOrFloat(property.getType(), property.getPRDRange()) ||
                    ((property.getPRDRange() != null) && !isRangeFiledsValid(property.getPRDRange())))
                return false;

            // add name to the map list
            prdNameList.put(trimmedPropName, (prdNameList.get(trimmedPropName) == null ?
                    1 : prdNameList.get(trimmedPropName + 1)));
        }

        // check if any name not unique
        return isNameUnique(prdNameList);

    }

    // test if prop name has no spaces
    // test if name is uniq
    // test if type is
}

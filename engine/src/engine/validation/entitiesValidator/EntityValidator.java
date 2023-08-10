package engine.validation.entitiesValidator;

import engine.validation.ValidationCommonFunctions;
import engine.validation.exceptions.XmlValidationException;
import generated.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityValidator extends ValidationCommonFunctions {
    //need to add exceptions
    public void validateEntityData(PRDEntity prdEntity) throws XmlValidationException{
        String trimmedPropName;
        List<PRDProperty> prdList = prdEntity.getPRDProperties().getPRDProperty(); //property list of specific entity
        Map<String, Integer> prdNameList = new HashMap<>(); // this map will be for validating all names are uniques
        String trimmedName = prdEntity.getName().trim();

        //check if entity name is invalid
        if (!isNameValid(trimmedName)) {
            throw new XmlValidationException("Entity: " + prdEntity.getName() +
                    " name is not valid, name should not have spaces");
        }

        // go over every property and check if valid
        for(PRDProperty property : prdList) {
            trimmedPropName = property.getPRDName().trim();

          /*  if(!isNameValid(trimmedPropName) ||
                    !isPropertyTypeValid(property.getType()) ||
                    !isRangeLegalWithDecimalOrFloat(property.getType(), property.getPRDRange()) ||
                    ((property.getPRDRange() != null) && !isRangeFiledsValid(property.getPRDRange())))
                return false;*/

            if (!isNameValid(trimmedPropName)) {
                throw new XmlValidationException("Entity: " + prdEntity.getName() +
                        ", property : " + property.getPRDName() +
                        " name is not valid, name should not have spaces");
            }
            if (!isPropertyTypeValid(property.getType())) {
                throw new XmlValidationException("Entity: " + prdEntity.getName() +
                        ", property : " + property.getPRDName() +
                        " type is not valid, only type decimal, float, string or boolean allowed");
            }
            if (!isRangeLegalWithDecimalOrFloat(property.getType(), property.getPRDRange())) {
                throw new XmlValidationException("Entity: " + prdEntity.getName() +
                        ", property : " + property.getPRDName() +
                        " is not valid, if type is not decimal or float range should not appear");
            }
            if ((property.getPRDRange() != null) && !isRangeFiledsValid(property.getPRDRange())) {
                throw new XmlValidationException("Entity: " + prdEntity.getName() +
                        ", property : " + property.getPRDName() +
                        "is not valid, from value should be smaller than to");
            }

            // add name to the map list
            prdNameList.put(trimmedPropName, (prdNameList.get(trimmedPropName) == null ?
                    1 : prdNameList.get(trimmedPropName + 1)));
        }

        // check if any name not unique
        if (!isNameUnique(prdNameList)) {
            throw new XmlValidationException("Invalid entity properties list, all entity's properties name need to have unique values");
        }
    }

    // test if prop name has no spaces
    // test if name is uniq
    // test if type is
}

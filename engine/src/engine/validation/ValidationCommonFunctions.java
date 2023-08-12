package engine.validation;

import generated.PRDRange;

import java.util.Map;

public class ValidationCommonFunctions {
    // validate if the name has no spaces
    public static boolean isNameValid(String name) {
        if(name.contains(" "))
            return false;
        return true;
    }

    // validate if name is uniqe
    public static boolean isNameUnique(Map<String, Integer> prdNameList){
        // check if any name not unique
        /*for (Map.Entry<String, Integer> entry : prdNameList.entrySet()) {
            if(entry.getValue() != 1)
                return false;
        }
        return true;*/
        return prdNameList.entrySet().stream()
                .allMatch(entry -> entry.getValue() == 1);
    }


    // validate type is only decimal/float/boolean/string
    public static boolean isPropertyTypeValid(String type) {
        return (type.contains("decimal") || type.contains("float") || type.contains("boolean") || type.contains("string"));
    }

    // validate range has values only if property type is decimal/float
    public static boolean isRangeLegalWithDecimalOrFloat(String type, PRDRange range){
        if(type.equals("decimal") || type.equals("float")) {
            if (range != null)
                return true;
        }
        else if (type.equals("string") || type.equals("boolean")) {
            if (range == null)
                return true;
        }
        return false;
    }

    // validate from is smaller value from to
    public static boolean isRangeFiledsValid(PRDRange range){
        if(range.getFrom() <= range.getTo())
            return true;
        return false;
    }
}

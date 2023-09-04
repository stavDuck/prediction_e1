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
        return prdNameList.entrySet().stream()
                .allMatch(entry -> entry.getValue() == 1);
    }

    // validate type is only decimal/float/boolean/string
    public static boolean isPropertyTypeValid(String type) {
        return (type.contains("decimal") || type.contains("float") || type.contains("boolean") || type.contains("string"));
    }

    // validate range has values only if property type is decimal/float
    public static boolean isRangeLegalWithDecimalOrFloat(String type, PRDRange range){
        //CHANGE - range is optional
        /*if(type.equals("decimal") || type.equals("float")) {
            if (range != null)
                return true;
        }
        if (type.equals("string") || type.equals("boolean")) {
            if (range == null)
                return true;
        }
        return false;*/
        if (type.equals("string") || type.equals("boolean")) {
            if (range != null)
                return false;
        }
        return true;

    }

    // validate from is smaller value from to
    public static boolean isRangeFiledsValid(PRDRange range){
        if(range.getFrom() <= range.getTo())
            return true;
        return false;
    }

    // validate if init val is in range
    public static boolean isInitValInRange(String init, PRDRange range, String type) {
        if(type.equals("decimal")){
            Integer number = Integer.parseInt(init);
            if(number < (int)range.getFrom() || number > (int)range.getTo())
               return false;
        }
        else if(type.equals("float")){
            Float number = Float.parseFloat(init);
            if(number < (float)range.getFrom() || number > (float)range.getTo())
               return false;
        }
        return true;
    }
}

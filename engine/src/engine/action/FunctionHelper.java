package engine.action;
import engine.execution.context.Context;
import engine.property.type.Type;
import engine.value.generator.ValueGeneratorFactory;

import java.util.Random;

public class FunctionHelper {
    public static Object environment(Context context, String nameProp) {
        return context.getEnvironmentVariable(nameProp).getVal();
    }

    // Function gets the string expression, the context (instanse, envList..) and the propName we need to set the value into
    public static Object getValueToInvoke(String expression, Context context, String propName) throws RuntimeException{
        Type typeProp = context.getPrimaryEntityInstance().getPropertyInstanceByName(propName).getType();
        Object value;

        // if value is in env func
        if(expression.contains("environment(")){
            value = FunctionHelper.environment(context,
                    extractStringFromEnviromentFunc(expression));
        }

        // if value is in random func - random can be only numeric
        else if(expression.contains("random(")){
            String randNum = extractStringFromRandomFunc(expression);

            switch (typeProp) {
                case DECIMAL:
                    value = ValueGeneratorFactory.createRandomInteger( 1, Integer.parseInt(randNum)).generateValue();
                    break;
                case FLOAT:
                    value = ValueGeneratorFactory.createRandomFloat((float)1, Float.parseFloat(randNum)).generateValue();
                    break;
                default:
                    throw new RuntimeException("invalid action - The expression has random() function but property type is not numeric");
            }
        }

        // if value is a property from the instance get the value
        else if(context.getPrimaryEntityInstance().getPropertyInstanceByName(expression) != null){
            value = context.getPrimaryEntityInstance().getPropertyInstanceByName(expression).getVal();
        }

        // in free style string - try to convert according to the target property type
        else{
            value = parserFromStringAccordingToType(expression, typeProp);
        }
        return value;
    }

    public static String extractStringFromEnviromentFunc(String input){
        return input.substring(
                input.indexOf("environment(") + 12,
                input.indexOf(")"));
    }

    public static String extractStringFromRandomFunc(String input){
        return  input.substring(
                input.indexOf("random(") + 7,
                input.indexOf(")"));
    }

    public static Object parserFromStringAccordingToType(String value, Type type){
            Object val;
            switch (type) {
                case DECIMAL:
                    val = Integer.parseInt(value);
                    break;
                case FLOAT:
                    val = Float.parseFloat(value);
                    break;
                case BOOLEAN:
                    val = Boolean.parseBoolean(value); //maybe need to add exception
                    break;
                default:
                    val = value;
            }
            return val;
    }

}

package engine.action;
import engine.execution.context.Context;
import engine.property.type.Type;
import java.util.Random;

public class FunctionHelper {
    public static Object environment(Context context, String nameProp) {
        return context.getEnvironmentVariable(nameProp).getVal();
    }
    public static int random(int number){
        return (new Random().nextInt(number + 1));
    }

    // return String
    public static Object getValueToInvoke(String expression, Context context, String propName){
        Object value;
        if(expression.contains("environment(")){
            value = FunctionHelper.environment(context,
                    expression.substring(
                            expression.indexOf("environment(") + 12,
                            expression.indexOf(")")));
        }
        else if(expression.contains("random(")){
            String randNum = expression.substring(
                    expression.indexOf("random(") + 7,
                    expression.indexOf(")"));
            value = FunctionHelper.random(Integer.parseInt(randNum));
        }
        // assuming sutibale int/ float
        else{
            Type typeProp = context.getPrimaryEntityInstance().getPropertyInstanceByName(propName).getType();
            value = parserFromStringAccordingToType(expression, typeProp);
        }
        return value;
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

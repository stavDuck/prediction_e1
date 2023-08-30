package engine.action;
import com.sun.corba.se.impl.ior.OldJIDLObjectKeyTemplate;
import engine.execution.context.Context;
import engine.property.type.Type;
import engine.value.generator.ValueGeneratorFactory;

public class FunctionHelper {
    public static Object environment(Context context, String nameProp) {
        return context.getEnvironmentVariable(nameProp).getVal();
    }

    // Old function
    // Function gets the string expression, the context (instanse, envList..) and the propName we need to set the value into
    public static Object getValueToInvoke(String expression, Context context, String propName) throws RuntimeException {
        return (getValueFromExpression(expression, context));
    }

    // new Function Get Value from Expression - Type is dynamic inside object
    public static Object getValueFromExpression(String expression, Context context) throws RuntimeException {
        Object value;

        // if value is in env func
        if (expression.contains("environment(")) {
            value = FunctionHelper.environment(context,
                    extractStringFromEnviromentFunc(expression));

        }

        // if Task 2 only Float avilable
        else if (expression.contains("random(")) {
            String randNum = extractStringFromRandomFunc(expression);
            value = ValueGeneratorFactory.createRandomFloat((float) 1, Float.parseFloat(randNum)).generateValue();
        }

        // NEED TO ADD EVALUATE , PRECENT, TICKS + TICKS SUPPORT
        else if (expression.contains("evaluate")) {
            String resVal = extraceStringFromEvaluateFunc(expression);
            String[] res = resVal.split(".");
            return context.getEntityInstanceManager().getInstancesByName(res[0]).get(0)
        } else if (expression.contains("percent")) {
            String resVal = extractStringFromPercentFunc(expression);
            String[] res = resVal.split(",");
            value = calculatePercent(res[0], res[1], context);
        } else if (expression.contains("ticks")) {
            String resVal = extractStringFromTicksFunc(expression);
            String[] res = resVal.split(".");
            value = calculateTicks(res[0], res[1]);
        }
        // if value is a property from the instance get the value
        else if (context.getPrimaryEntityInstance().getPropertyInstanceByName(expression) != null) {
            value = context.getPrimaryEntityInstance().getPropertyInstanceByName(expression).getVal();
        }

        // In Task 2 if free txt stay in String format
        else {
            value = expression;
        }
        return value;
    }

    public static Integer calculatePercent(String exp1, String exp2, Context context) {
        Object arg1 = getValueFromExpression(exp1, context);
        Object arg2 = getValueFromExpression(exp2, context);
        return ((Integer)arg1 * (Integer) arg2) / 100;
    }

    public static Integer calculateTicks(String entity, String property) {

    }
    public static String extractStringFromEnviromentFunc(String input) {
        return input.substring(
                input.indexOf("environment(") + 12,
                input.indexOf(")"));
    }
    public static String extractStringFromTicksFunc(String input) {
        return input.substring(
                input.indexOf("ticks(") + 6,
                input.indexOf(")"));
    }

    public static String extractStringFromPercentFunc(String input) {
        return input.substring(
                input.indexOf("percent(") + 8,
                input.indexOf(")"));
    }

    public static String extraceStringFromEvaluateFunc(String input) {
        return input.substring(input.indexOf("evaluate(") + 9,
                input.indexOf(")"));
    }

    public static String extractStringFromRandomFunc(String input) {
        return input.substring(
                input.indexOf("random(") + 7,
                input.indexOf(")"));
    }

    public static Object parserFromStringAccordingToType(String value, Type type) {
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

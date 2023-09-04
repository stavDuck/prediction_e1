package engine.action;
import com.sun.corba.se.impl.ior.OldJIDLObjectKeyTemplate;
import engine.entity.EntityInstance;
import engine.execution.context.Context;
import engine.property.PropertyInstance;
import engine.property.type.Type;
import engine.validation.exceptions.XmlValidationException;
import engine.value.generator.ValueGeneratorFactory;
import generated.PRDEntities;
import generated.PRDEntity;
import generated.PRDProperty;

import java.util.Optional;

public class FunctionHelper {
    public static Object environment(Context context, String nameProp) {
        return context.getEnvironmentVariable(nameProp).getVal();
    }

    // Function get an expression string, context and get the property correct name

    // get used only by single condition with property expression
    public static Object getPropertyExpression(String expression, Context context) {
        return getValueFromExpression(expression, context);
    }

    // Old function
    // Function gets the string expression, the context (instanse, envList..) and the propName we need to set the value into
    public static Object getValueToInvoke(String expression, Context context, String propName) throws RuntimeException {
        //return (getValueFromExpression(expression, context));
        return getValueToInvokeWithProp(expression, context, propName);
    }

    // new Function Get Value from Expression - Type is dynamic inside object
    public static Object getValueFromExpression(String expression, Context context) throws RuntimeException {
        Object value;

        // if value is in env func
        if (expression.startsWith("environment(")) {
            value = FunctionHelper.environment(context,
                    extractStringFromEnviromentFunc(expression));
        }

        // if Task 2 only Float avilable
        else if (expression.startsWith("random(")) {
            String randNum = extractStringFromRandomFunc(expression);
            value = ValueGeneratorFactory.createRandomFloat((float) 1, Float.parseFloat(randNum)).generateValue();
        }

        else if (expression.startsWith("evaluate")) {
            String resVal = extraceStringFromEvaluateFunc(expression);
            value = getEvaluateProperty(resVal, context).getVal();
        } else if (expression.startsWith("percent")) {
            String resVal = extractStringFromPercentFunc(expression);
            String[] res = resVal.split(",");
            value = calculatePercent(res[0], res[1], context);
        } else if (expression.startsWith("ticks")) {
            String resVal = extractStringFromTicksFunc(expression);
            value = calculateTicks(resVal, context);
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

    public static Object getValueToInvokeWithProp(String expression, Context context, String propName) throws RuntimeException {
        Type typeProp = context.getPrimaryEntityInstance().getPropertyInstanceByName(propName).getType();
        Object value;

        // if value is in env func
        if(expression.startsWith("environment(")){
            value = FunctionHelper.environment(context,
                    extractStringFromEnviromentFunc(expression));
        }

        // if value is in random func - random can be only numeric
        else if(expression.startsWith("random(")){
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
        else if (expression.startsWith("evaluate")) {
            String resVal = extraceStringFromEvaluateFunc(expression);
            value = getEvaluateProperty(resVal, context).getVal();
        } else if (expression.startsWith("percent")) {
            String resVal = extractStringFromPercentFunc(expression);
            String[] res = resVal.split(",");
            value = calculatePercent(res[0], res[1], context);
        } else if (expression.startsWith("ticks")) {
            String resVal = extractStringFromTicksFunc(expression);
            value = calculateTicks(resVal, context);
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
    public static Object getValueToInvokeByType(String expression, Context context, Type type) throws RuntimeException {
        return getValueToInvokeFromType(expression, context, type);
    }

    public static Object getValueToInvokeFromType(String expression, Context context, Type type) {
        Object value;

        // if value is in env func
        if (expression.startsWith("environment(")) {
            value = FunctionHelper.environment(context,
                    extractStringFromEnviromentFunc(expression));
        }

        // if Task 2 only Float avilable
        else if (expression.startsWith("random(")) {
            String randNum = extractStringFromRandomFunc(expression);
            value = ValueGeneratorFactory.createRandomFloat((float) 1, Float.parseFloat(randNum)).generateValue();
        }

        else if (expression.startsWith("evaluate")) {
            String resVal = extraceStringFromEvaluateFunc(expression);
            value = getEvaluateProperty(resVal, context).getVal();
        } else if (expression.startsWith("percent")) {
            String resVal = extractStringFromPercentFunc(expression);
            String[] res = resVal.split(",");
            value = calculatePercent(res[0], res[1], context);
        } else if (expression.startsWith("ticks")) {
            String resVal = extractStringFromTicksFunc(expression);
            value = calculateTicks(resVal, context);
        }
        // if value is a property from the instance get the value
        else if (context.getPrimaryEntityInstance().getPropertyInstanceByName(expression) != null) {
            value = context.getPrimaryEntityInstance().getPropertyInstanceByName(expression).getVal();
        }

        // In Task 2 if free txt stay in String format
        else {
            value = parserFromStringAccordingToType(expression, type);
        }
        return value;

    }

    public static Integer calculatePercent(String exp1, String exp2, Context context) throws RuntimeException{
        Object arg1 = getValueFromExpression(exp1, context);
        Object arg2 = getValueFromExpression(exp2, context);
        if (arg1 instanceof Number && arg2 instanceof Number) {
            int intArg1 = (arg1 instanceof Float) ? Math.round((Float) arg1) : (Integer) arg1;
            int intArg2 = (arg2 instanceof Float) ? Math.round((Float) arg2) : (Integer) arg2;

            return Math.round((intArg1 * intArg2) / 100);
        } else {
            throw new RuntimeException("Arguments provided to 'Percent' function helper are not numeric");
        }


    }

    public static Integer calculateTicks(String expression, Context context) {
        //need to get the property using the evaluate func
        PropertyInstance propertyInstance  = getEvaluateProperty(expression, context);
        return (context.getCurrTick() - propertyInstance.getLastEndTick());
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
                input.indexOf("percent(") + 8);
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

    public static PropertyInstance getEvaluateProperty(String expression, Context context) {
        String[] evaluateExp = expression.split("\\.");
        EntityInstance entity;
        PropertyInstance property = null;

        //if there's a secondary entity
        if (context.getSecondaryEntityInstance() != null && evaluateExp[0].equalsIgnoreCase(context.getSecondaryEntityInstance().getEntityName())) {
            entity = context.getSecondaryEntityInstance();
        }
        //if there isn't a secondary, there will be primary
        else {
            entity = context.getPrimaryEntityInstance();
        }
        property = entity.getPropertyInstanceByName(evaluateExp[1]);
        return property;
    }


}

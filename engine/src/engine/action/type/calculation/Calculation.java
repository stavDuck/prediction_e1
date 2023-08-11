package engine.action.type.calculation;

import engine.action.AbstractAction;
import engine.action.FunctionHelper;
import engine.execution.context.Context;
import engine.property.PropertyInstance;
import engine.property.type.Type;

public class Calculation extends AbstractAction {
    final static String MULTIPLY = "multiply";
    final static String DIVIDE = "divide";

    private String resultProp;
    private String arg1;
    private String arg2;
    private String operatorType; // can be only multiply or divide

    public Calculation(String entityName, String actionType, String operatorType,
                       String resultProp, String arg1, String arg2) {
        super(entityName, actionType);
        this.operatorType = operatorType;
        this.resultProp = resultProp;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public void invoke(Context context) {
        Object val1 = FunctionHelper.getValueToInvoke(arg1, context, resultProp);
        Object val2 = FunctionHelper.getValueToInvoke(arg2, context, resultProp);

        // send the actual values in object format and the res-prop to set the new value into
        switch (operatorType){
            case MULTIPLY:
                multiplyFunction(context.getPrimaryEntityInstance().getPropertyInstanceByName(resultProp), val1, val2);
                break;
            case DIVIDE:
                divideFunction(context.getPrimaryEntityInstance().getPropertyInstanceByName(resultProp), val1, val2);
                break;
        }
    }

    private void divideFunction(PropertyInstance prop, Object val1, Object val2){
        // check the operation needed
        Type entityType = prop.getType();

        switch (entityType){
            case DECIMAL:
                Integer int1 = Type.DECIMAL.convert(val1);
                Integer int2 = Type.DECIMAL.convert(val2);
                prop.setVal( int1 / int2);
                break;
            case FLOAT:
                Float float1 = Type.FLOAT.convert(val1);
                Float float2 = Type.FLOAT.convert(val2);
                prop.setVal( float1 / float2);
                break;
        }
    }

    private void multiplyFunction(PropertyInstance prop, Object val1, Object val2){
        // check the operation needed
        Type entityType = prop.getType();

        switch (entityType){
            case DECIMAL:
                Integer int1 = Type.DECIMAL.convert(val1);
                Integer int2 = Type.DECIMAL.convert(val2);
                prop.setVal( int1 * int2);
                break;
            case FLOAT:
                Float float1 = Type.FLOAT.convert(val1);
                Float float2 = Type.FLOAT.convert(val2);
                prop.setVal( float1 * float2);
                break;
        }
    }
}
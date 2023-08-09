package engine.action.type.calculation.math.operation;

import engine.action.AbstractAction;
import engine.action.FunctionHelper;
import engine.execution.context.Context;
import engine.property.Property;
import engine.property.PropertyInstance;
import engine.property.type.Type;

public class Multiply extends AbstractAction {
    private String resultProp;
    private String arg1;
    private String arg2;

    public Multiply(String entityName, String actionType) {
        super(entityName, actionType);
    }

    @Override
    public void invoke(Context context) {
        Object val1 = FunctionHelper.getValueToInvoke(arg1, context, resultProp);
        Object val2 = FunctionHelper.getValueToInvoke(arg2, context, resultProp);

        // send the actual values in object format and the res-prop to set the new value into
        multiplyFunction(context.getPrimaryEntityInstance().getPropertyInstanceByName(resultProp), val1, val2);
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

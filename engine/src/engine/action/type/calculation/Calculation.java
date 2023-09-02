package engine.action.type.calculation;
import engine.action.AbstractAction;
import engine.action.FunctionHelper;
import engine.action.type.condition.Condition;
import engine.execution.context.Context;
import engine.property.PropertyInstance;
import engine.property.type.Type;

public class Calculation extends AbstractAction {
    protected final static String MULTIPLY = "multiply";
    protected final static String DIVIDE = "divide";

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

    // ctor for secondary
    public Calculation(String entityName, String actionType, String operatorType,
                       String resultProp, String arg1, String arg2, int secondaryAmount, String secondaryEntityName, Condition condition, boolean isSelectedAll) {
        super(entityName, actionType, secondaryAmount, secondaryEntityName, condition, isSelectedAll);
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
                multiplyFunction(context.getPrimaryEntityInstance().getPropertyInstanceByName(resultProp), val1, val2, context);
                break;
            case DIVIDE:
                try {
                    divideFunction(context.getPrimaryEntityInstance().getPropertyInstanceByName(resultProp), val1, val2, context);
                }
                catch (ArithmeticException e){
                    throw new RuntimeException("Division failed with an error: " + e.getMessage());
                }
                break;
        }
    }

    private void divideFunction(PropertyInstance prop, Object val1, Object val2, Context context){
        // check the operation needed
        Type entityType = prop.getType();
        Float float1, float2;
        switch (entityType){
            case DECIMAL:
                Integer int1 = Type.DECIMAL.convert(val1);
                Integer int2 = Type.DECIMAL.convert(val2);
                if(int2 == 0) {
                    throw new ArithmeticException("The denominator equals to 0, can a number can't be divided by 0");
                }
                if((int1 / int2) >= prop.getRange().getFrom() && (int1 / int2) <= prop.getRange().getTo())
                    prop.setVal( int1 / int2);

                prop.setVal( int1 / int2);
                break;
            case FLOAT:
                if(val1 instanceof Integer) {
                    float1 = Float.valueOf(val1.toString());
                }

                else {
                    float1 = Type.FLOAT.convert(val1);
                }
                if(val2 instanceof Integer) {
                    float2 = Float.valueOf(val2.toString());
                }

                else {
                    float2 = Type.FLOAT.convert(val2);
                }                if(float2 == 0) {
                    throw new ArithmeticException("The denominator equals to 0, can a number can't be divided by 0");
                }
                if((float1 / float2) >= prop.getRange().getFrom() && (float1 / float2) <= prop.getRange().getTo())
                    prop.setVal( float1 / float2);
                prop.setVal( float1 / float2);
                break;
        }
        prop.setNewTickHistory(prop.getLastEndTick(), context.getCurrTick());

    }

    private void multiplyFunction(PropertyInstance prop, Object val1, Object val2, Context context){
        // check the operation needed
        Type entityType = prop.getType();
        Float float2, float1;
        switch (entityType){
            case DECIMAL:
                Integer int1 = Type.DECIMAL.convert(val1);
                Integer int2 = Type.DECIMAL.convert(val2);
                if((int1 * int2) >= prop.getRange().getFrom() && (int1 * int2) <= prop.getRange().getTo())
                    prop.setVal( int1 * int2);
                break;
            case FLOAT:
                if(val1 instanceof Integer) {
                    float1 = Float.valueOf(val1.toString());
                }

                else {
                    float1 = Type.FLOAT.convert(val1);
                }
                if(val2 instanceof Integer) {
                    float2 = Float.valueOf(val2.toString());
                }

                else {
                    float2 = Type.FLOAT.convert(val2);
                }
                if((float1 * float2) >= prop.getRange().getFrom() && (float1 * float2) <= prop.getRange().getTo())
                    prop.setVal( float1 * float2);
                break;
        }
        prop.setNewTickHistory(prop.getLastEndTick(), context.getCurrTick());
    }

    public String getResultProp() {
        return resultProp;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getOperatorType() {
        return operatorType;
    }
}

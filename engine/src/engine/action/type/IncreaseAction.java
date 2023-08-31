package engine.action.type;

import engine.action.AbstractAction;
import engine.action.FunctionHelper;
import engine.action.type.condition.Condition;
import engine.execution.context.Context;
import engine.property.PropertyInstance;
import engine.property.type.Type;

public class IncreaseAction extends AbstractAction {
    private String property;
    private String byExpression;

    public IncreaseAction(String entityName, String property, String actionType, String byExpression) {
        super(entityName, actionType);
        this.property = property;
        this.byExpression = byExpression;
    }
    //ctor for secondary
    public IncreaseAction(String entityName, String property, String actionType, String byExpression, int secondaryAmount, String secondaryEntityName, Condition condition, boolean isSelectedAll) {
        super(entityName, actionType, secondaryAmount, secondaryEntityName, condition, isSelectedAll);
        this.property = property;
        this.byExpression = byExpression;
    }
    @Override
    public void invoke(Context context) throws RuntimeException{
        try {
            Object value = FunctionHelper.getValueToInvoke(byExpression, context, property);
            increasePropertyValWithVal(context.getPrimaryEntityInstance().getPropertyInstanceByName(property), value);
        }
        catch (RuntimeException e){
            throw new RuntimeException("Increase action on Entity: " + entityName + " on property: " + property +  " failed \n"
                    +  e.getMessage());
        }
    }

    private void increasePropertyValWithVal(PropertyInstance prop, Object increaseVal){
        Type propType = prop.getType();
        Object propVal = prop.getVal();
        Float float2;
        switch (propType){
            case DECIMAL:
                Integer int1 = Type.DECIMAL.convert(propVal);
                Integer int2 = Type.DECIMAL.convert(increaseVal);
                //check that the increase stays in range
                if((int1 + int2) >= prop.getRange().getFrom() && (int1 + int2) <= prop.getRange().getTo())
                    prop.setVal( int1 + int2);
                break;
            case FLOAT:
                Float float1 = Type.FLOAT.convert(propVal);
                if(increaseVal instanceof Integer) {
                    float2 = Float.valueOf(increaseVal.toString());
                }

                else {
                    float2 = Type.FLOAT.convert(increaseVal);
                }
                if(((float1 + float2) >= prop.getRange().getFrom()) && ((float1 + float2) <= prop.getRange().getTo()))
                    prop.setVal( float1 + float2);
                break;
        }
    }
}

package engine.action.type;
import engine.action.AbstractAction;
import engine.action.FunctionHelper;
import engine.action.type.condition.Condition;
import engine.execution.context.Context;
import engine.property.PropertyInstance;
import engine.property.type.Type;

public class DecreaseAction extends AbstractAction {

    private String property;
    private String byExpression;

    public DecreaseAction(String entityName, String property, String actionType, String byExpression) {
        super(entityName, actionType);
        this.property = property;
        this.byExpression = byExpression;
    }
    // ctor for secondary
    public DecreaseAction(String entityName, String property, String actionType, String byExpression, int secondaryAmount, String secondaryEntityName, Condition condition, boolean isSelectedAll) {
        super(entityName, actionType, secondaryAmount, secondaryEntityName, condition, isSelectedAll);
        this.property = property;
        this.byExpression = byExpression;
    }
    @Override
    public void invoke(Context context) throws RuntimeException{
        try {
            Object value = FunctionHelper.getValueToInvoke(byExpression, context, property);
            if(context.getSecondaryEntityInstance() != null && entityName.equalsIgnoreCase(context.getSecondaryEntityInstance().getEntityName())) {
                decreasePropertyValWithVal(context.getSecondaryEntityInstance().getPropertyInstanceByName(property), value, context);
            }
            else {
                decreasePropertyValWithVal(context.getPrimaryEntityInstance().getPropertyInstanceByName(property), value, context);
            }
        }
        catch (RuntimeException e){
            throw new RuntimeException("Decrease action on Entity: " + entityName + " on property: " + property +  " failed \n"
                    +  e.getMessage());
        }
    }

    private void decreasePropertyValWithVal(PropertyInstance prop, Object increaseVal, Context context){
        Type propType = prop.getType();
        Object propVal = prop.getVal();
        Float float2;
        switch (propType){
            case DECIMAL:
                Integer int1 = Type.DECIMAL.convert(propVal);
                Integer int2 = Type.DECIMAL.convert(increaseVal);
                if((int1 - int2) >= prop.getRange().getFrom() && (int1 - int2) <= prop.getRange().getTo()) {
                    prop.setVal(int1 - int2);
                    prop.setNewTickHistory(prop.getLastEndTick(), context.getCurrTick());
                }
                break;
            case FLOAT:
                Float float1 = Type.FLOAT.convert(propVal);
                if(increaseVal instanceof Integer) {
                    float2 = Float.valueOf(increaseVal.toString());
                }

                else {
                    float2 = Type.FLOAT.convert(increaseVal);
                }
                if(((float1 - float2) >= prop.getRange().getFrom()) && ((float1 - float2) <= prop.getRange().getTo())) {
                    prop.setVal(float1 - float2);
                    prop.setNewTickHistory(prop.getLastEndTick(), context.getCurrTick());
                }
                break;
        }
    }

    public String getProperty() {
        return property;
    }

    public String getByExpression() {
        return byExpression;
    }
}

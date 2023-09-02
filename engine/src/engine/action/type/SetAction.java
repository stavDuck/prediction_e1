package engine.action.type;

import engine.action.AbstractAction;
import engine.action.FunctionHelper;
import engine.action.type.condition.Condition;
import engine.execution.context.Context;
import engine.property.PropertyInstance;

import static engine.property.type.Type.BOOLEAN;

public class SetAction extends AbstractAction {
    private final String property;
    private String newValue;

    public SetAction(String entityName, String property, String actionType, String newValue) {
        super(entityName, actionType);
        this.property = property;
        this.newValue = newValue;
    }

    // ctor for action with secondary
    public SetAction(String entityName, String property, String actionType, String newValue, int secondaryAmount, String secondaryEntityName, Condition condition, boolean isSelectedAll) {
        super(entityName, actionType,secondaryAmount, secondaryEntityName, condition, isSelectedAll);
        this.property = property;
        this.newValue = newValue;
    }

    @Override
    public void invoke(Context context) {
        Object value = FunctionHelper.getValueToInvoke(newValue, context, property);
        PropertyInstance prop;
        if(context.getSecondaryEntityInstance() != null && entityName.equalsIgnoreCase(context.getSecondaryEntityInstance().getEntityName())) {
            prop = context.getSecondaryEntityInstance().getPropertyInstanceByName(property);
        }
        else {
            prop = context.getPrimaryEntityInstance().getPropertyInstanceByName(property);
        }
        if (prop.getRange() != null) {
            // check if out of range for decimal
            if (prop.getType().name().toLowerCase().equals("decimal")) {
                if ((int) value < (int) prop.getRange().getFrom() ||
                        (int) value > (int) prop.getRange().getTo())
                    value = null;
            } else if (prop.getType().name().toLowerCase().equals("float")) {
                if ((float) value < (float) prop.getRange().getFrom() ||
                        (float) value > (float) prop.getRange().getTo())
                    value = null;
            }
        }

        // if prop is boolean - can get string "true" or "false"
        else if(prop.getType() == BOOLEAN && (value instanceof String)){
            if(((String)value).equals("true")){
              prop.setVal(true);
            }
            else if(((String)value).equals("false")){
                prop.setVal(false);
            }
        }

        if (value != null) {
            prop.setVal(value);
            prop.setNewTickHistory(prop.getLastEndTick(), context.getCurrTick());
        }
    }
}
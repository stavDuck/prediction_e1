package engine.action.type;

import engine.action.AbstractAction;
import engine.action.FunctionHelper;
import engine.execution.context.Context;
import engine.property.PropertyInstance;

public class SetAction extends AbstractAction {
    private final String property;
    private String newValue;


    public SetAction(String entityName, String property, String actionType, String newValue) {
        super(entityName, actionType);
        this.property = property;
        this.newValue = newValue;
    }

    @Override
    public void invoke(Context context) {
        Object value = FunctionHelper.getValueToInvoke(newValue, context, property);

        PropertyInstance prop = context.getPrimaryEntityInstance()
                .getPropertyInstanceByName(property);

        if (prop.getRange() != null) {
            // check if out of range for decimal
            if (prop.getType().name().toLowerCase().equals("decimal")) {
                if ((int) value < (int) prop.getRange().getFrom() ||
                        (int) value > (int) prop.getRange().getTo())
                    value = null;
            } else if (prop.getType().name().toLowerCase().equals("decimal")) {
                if ((float) value < (float) prop.getRange().getFrom() ||
                        (float) value > (float) prop.getRange().getTo())
                    value = null;
            }
        }

        if (value != null) {
            context.getPrimaryEntityInstance().getPropertyInstanceByName(property).setVal(value);
        }
    }
}

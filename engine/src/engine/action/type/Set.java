package engine.action.type;

import engine.action.AbstractAction;
import engine.action.FunctionHelper;
import engine.execution.context.Context;

public class Set extends AbstractAction {
    private final String property;
    private String newValue;


    public Set(String entityName, String property, String actionType, String newValue) {
        super(entityName, actionType);
        this.property = property;
        this.newValue = newValue;
    }

    @Override
    public void invoke(Context context) {
        // NEED TO TEST ?
        Object value =  FunctionHelper.getValueToInvoke(newValue, context);
       context.getPrimaryEntityInstance().getPropertyInstanceByName(property).setVal(value);
    }
}

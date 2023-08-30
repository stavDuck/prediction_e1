package engine.action.type.condition;
import engine.action.AbstractAction;
import engine.action.ActionType;
import engine.action.FunctionHelper;
import engine.execution.context.Context;
import engine.property.type.Type;

public class ConditionSingle extends AbstractAction implements conditionSingularityApi {
    private static final String EQUALS = "=";
    private static final String NOT_EUQALS = "!=";
    private static final String BT = "bt";
    private static final String LT = "lt";

    // methods
    private String entityToInvoke;
    private String propertyToInvoke;
    private Operator op;
   private String value;
   private boolean result;

   public ConditionSingle(String entityName, String actionType, String entityToInvoke,
                          String propertyToInvoke,String operator, String value){
       super(entityName, actionType);
       this.entityToInvoke = entityToInvoke;
       this.propertyToInvoke = propertyToInvoke;
       setOperatorFromString(operator);
       this.value = value;
       result = true;
   }

    public ConditionSingle(String entityName, ActionType actionType, String entityToInvoke,
                           String propertyToInvoke,String operator, String value){
        super(entityName, actionType);
        this.entityToInvoke = entityToInvoke;
        this.propertyToInvoke = propertyToInvoke;
        setOperatorFromString(operator);
        this.value = value;
        result = true;
    }
    // ctors for secondary
    public ConditionSingle(String entityName, String actionType, String entityToInvoke,
                           String propertyToInvoke,String operator, String value, int secondaryAmount, String secondaryEntityName){
        super(entityName, actionType, secondaryAmount,secondaryEntityName);
        this.entityToInvoke = entityToInvoke;
        this.propertyToInvoke = propertyToInvoke;
        setOperatorFromString(operator);
        this.value = value;
        result = true;
    }

    public ConditionSingle(String entityName, ActionType actionType, String entityToInvoke,
                           String propertyToInvoke,String operator, String value, int secondaryAmount, String secondaryEntityName){
        super(entityName, actionType, secondaryAmount, secondaryEntityName);
        this.entityToInvoke = entityToInvoke;
        this.propertyToInvoke = propertyToInvoke;
        setOperatorFromString(operator);
        this.value = value;
        result = true;
    }
    // getters
    public String getEntityToInvoke() {
        return entityToInvoke;
    }
    public String getPropertyToInvoke() {
        return propertyToInvoke;
    }
    public Operator getOp() {
        return op;
    }
    public String getValue() {
        return value;
    }

    @Override
    public String getSingularity() {
        return "single";
    }
    @Override
    public boolean getResult() {
        return result;
    }

    // setters
    public void setEntityToInvoke(String entityToInvoke) {
        this.entityToInvoke = entityToInvoke;
    }
    public void setPropertyToInvoke(String propertyToInvoke) {
        this.propertyToInvoke = propertyToInvoke;
    }
    public void setOp(Operator op) {
        this.op = op;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setOperatorFromString(String op){
        switch (op){
            case EQUALS:
                this.op = Operator.EQUALS;
                break;
            case NOT_EUQALS:
                this.op = Operator.NOT_EQUALS;
                break;
            case BT:
                this.op = Operator.BT;
                break;
            case LT:
                this.op = Operator.LT;
                break;
            default:
                this.op = null;
        }
    }

    @Override
    public void invoke(Context context) throws RuntimeException{
      // addition to task 2
        // get property value in Ojbect form - actual type behind
        // get value for condition by prop type

       Object propVal = context.getPrimaryEntityInstance().getPropertyInstanceByName(propertyToInvoke).getVal();
       Type propType = context.getPrimaryEntityInstance().getPropertyInstanceByName(propertyToInvoke).getType();

      // Object valueForCondition = parseByTypeAndString(propType, value);
        Object valueForCondition = FunctionHelper.getValueToInvoke(value, context, propertyToInvoke);
        result = evaluateCondition(propType, propVal,  valueForCondition);
    }

    private Object parseByTypeAndString(Type type, String defVal) {
        switch (type) {
            case DECIMAL:
                return Integer.parseInt(defVal);
            case FLOAT:
                return Float.parseFloat(defVal);
            case BOOLEAN:
                return Boolean.parseBoolean(defVal);
            default: // String type
                return defVal;
        }
    }

    public boolean evaluateCondition(Type type, Object propVal,  Object valueForCondition){
       switch (op){
           case EQUALS:
               return evaluateEquals(type, propVal, valueForCondition);
           case NOT_EQUALS:
               return evaluateNotEquals(type, propVal, valueForCondition);
           case BT:
               if(type == Type.STRING || type == Type.BOOLEAN)
                   throw new RuntimeException("single condition error: type: " + type.name() + " cannot be used with operator 'bt'");
               return evaluateBt(type, propVal, valueForCondition);
           case LT:
               if(type == Type.STRING || type == Type.BOOLEAN)
                   throw new RuntimeException("single condition error: type: " + type.name() + " cannot be used with operator 'lt'");
               return evaluateLt(type, propVal, valueForCondition);
           default:
               return true;
       }
    }

    public boolean evaluateEquals(Type type, Object propVal,  Object valueForCondition){
       switch (type){
           case DECIMAL:
               return ((Integer)propVal == (Integer)valueForCondition);
           case FLOAT:
               return ((Float)propVal == (Float)valueForCondition);
           case BOOLEAN:
               return ((Boolean) propVal == (Boolean) valueForCondition);
           case STRING:
               return (((String)propVal).equals((String) valueForCondition));
           default:
               return false;
       }
    }
    public boolean evaluateNotEquals(Type type, Object propVal,  Object valueForCondition){
        switch (type){
            case DECIMAL:
                return ((Integer)propVal != (Integer)valueForCondition);
            case FLOAT:
                return ((Float)propVal != (Float)valueForCondition);
            case BOOLEAN:
                return ((Boolean) propVal != (Boolean) valueForCondition);
            case STRING:
                return !(((String)propVal).equals((String) valueForCondition));
            default:
                return false;
        }
    }
    public boolean evaluateBt(Type type, Object propVal, Object valueForCondition){
        switch (type){
            case DECIMAL:
                return ((Integer)propVal > (Integer)valueForCondition);
            case FLOAT:
                return ((Float)propVal > (Float)valueForCondition);
            default:
                return false;
        }
    }
    public boolean evaluateLt(Type type, Object propVal, Object valueForCondition){
        switch (type){
            case DECIMAL:
                return ((Integer)propVal < (Integer)valueForCondition);
            case FLOAT:
                return ((Float)propVal < (Float)valueForCondition);
            default:
                return false;
        }
    }
}

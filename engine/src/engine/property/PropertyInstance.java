package engine.property;
import engine.property.tickhistory.TickHistory;
import engine.property.type.Type;
import engine.range.Range;

import java.util.ArrayList;
import java.util.List;

public class PropertyInstance extends Property {
    private Object val;
    private Object originalValueFromUser;
    private boolean isUserSetValue;
    private List<TickHistory> tickHistory;
    // doing create new instance for new entity
    public PropertyInstance(String name, Type type, Range range, Object value) {
        super(name, type, range);
        this.val = value;
        isUserSetValue = false;
        this.tickHistory = new ArrayList<>();
    }

    // doing create new env property
    public PropertyInstance(String name, String type, Range range, Object value) {
        super(name, type, range);
        this.val = value;
        isUserSetValue = false;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object value) {
        this.val = value;
    }

    public void setOriginalValueFromUser(Object value) {
        this.originalValueFromUser = value;
    }
    public void setVal(String value) throws NumberFormatException {
       try {
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
               case STRING:
                   val = value;
           }
       }
       catch (NumberFormatException e) {
           throw new NumberFormatException();
       }
    }

    public void setOriginalValueFromUser(String value) throws NumberFormatException {
        try {
            switch (type) {
                case DECIMAL:
                    originalValueFromUser = Integer.parseInt(value);
                    break;
                case FLOAT:
                    originalValueFromUser = Float.parseFloat(value);
                    break;
                case BOOLEAN:
                    originalValueFromUser = Boolean.parseBoolean(value); //maybe need to add exception
                    break;
                case STRING:
                    originalValueFromUser = value;
            }
        }
        catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
    }

    public Object getOriginalValueFromUser() {
        return originalValueFromUser;
    }

    public void printVal(Type type){
        switch (type) {
            case DECIMAL:
               System.out.println("Property value: " + (Integer) val);
                break;
            case FLOAT:
                System.out.println("Property value: " + (Float) val);
                break;
            case BOOLEAN:
                System.out.println("Property value: " + (Boolean) val);
                break;
            case STRING:
                System.out.println("Property value: " + (String) val);
        }
    }

    public boolean isUserSetValue() {
        return isUserSetValue;
    }

    public void setUserSetValue(boolean userSetValue) {
        isUserSetValue = userSetValue;
    }

    public List<TickHistory> getTickHistory() {
        return tickHistory;
    }

    public void setTickHistory(List<TickHistory> tickHistory) {
        this.tickHistory = tickHistory;
    }

    //gets the latest tick the property changed in
    public int getLastEndTick() {
        if(tickHistory.isEmpty()) {
            return 0;
        }
        return tickHistory.get(tickHistory.size() - 1).getEndTick();
    }

    public void setNewTickHistory(int startTick, int endTick) {
        this.tickHistory.add(new TickHistory(startTick, endTick));
    }
}

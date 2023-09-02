package engine.action.type;

import engine.Position;
import engine.action.AbstractAction;
import engine.action.FunctionHelper;
import engine.action.type.condition.Condition;
import engine.entity.EntityInstance;
import engine.execution.context.Context;
import engine.property.type.Type;

import java.util.ArrayList;
import java.util.List;

public class ProximityAction extends AbstractAction {
    private String targetEntity;
    private String envDepthOf;
    private List<AbstractAction> actions;

    public ProximityAction(String sourceEntity, String actionType, String targetEntity, String envDepthOf) {
        //in this action, kill is the main entity that's in the abstractAction class
        super(sourceEntity, actionType);
        this.targetEntity = targetEntity;
        this.envDepthOf = envDepthOf;
        this.actions = new ArrayList<>();
    }

    // ctor for secondary entity
    public ProximityAction(String sourceEntity, String actionType, String targetEntity, String envDepthOf, int secondaryAmount, String secondaryEntityName, Condition condition, boolean isSelectedAll) {
        //in this action, kill is the main entity that's in the abstractAction class
        super(sourceEntity, actionType, secondaryAmount, secondaryEntityName, condition, isSelectedAll);
        this.targetEntity = targetEntity;
        this.envDepthOf = envDepthOf;
        this.actions = new ArrayList<>();
    }
    @Override
    public void invoke(Context context) throws RuntimeException {
        Object calculatedOfExp = FunctionHelper.getValueToInvokeFromType(envDepthOf, context, Type.FLOAT);
        if (calculatedOfExp instanceof Float) {
            calculatedOfExp = Math.round((Float) calculatedOfExp);
        } else if (calculatedOfExp instanceof Integer) {
            calculatedOfExp = (Integer) calculatedOfExp;
        } else {
            throw new RuntimeException("Depth value in proximity action is: " + calculatedOfExp + " which is not numeric");
        }
        EntityInstance entityInstance = checkPointProximity(context.getPrimaryEntityInstance().getPos(), (Integer) calculatedOfExp, context);
        if (entityInstance != null) {
            context.setSecondaryEntityInstance(entityInstance);
            for (AbstractAction currAction : this.actions) {
                currAction.invoke(context);
            }
        }

    }

    public EntityInstance checkPointProximity(Position source, int proximityCircle, Context context) {
        int maxX = context.getGrid().getRows();
        int maxY = context.getGrid().getColumns();

        //go over all circles until max circle
        for (int radius = 1; radius <= proximityCircle; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    if (dx * dx + dy * dy <= radius * radius) {
                        /*int nx = x + dx;
                        int ny = y + dy;*/
                        int nx = (source.getX() % maxX) + dx;
                        int ny = source.getY() % maxY + dy;


                        // Check if the coordinates are within the grid boundaries
                        if (nx >= 0 && nx < maxX && ny >= 0 && ny < maxY) {
                            EntityInstance ret = context.getGrid().getPositionInGridBoard(nx, ny);
                            if (ret != null && ret.getEntityName().equalsIgnoreCase(targetEntity)) {
                                return ret;
                            }
                        }
                    }
                }

            }

        }
        return null;
    }

    public void addAction(AbstractAction action) {
        this.actions.add(action);
    }
}

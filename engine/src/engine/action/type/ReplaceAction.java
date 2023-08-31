package engine.action.type;

import engine.action.AbstractAction;
import engine.action.type.condition.Condition;
import engine.execution.context.Context;

public class ReplaceAction extends AbstractAction {
    private final static String SCRATCH = "scratch";
    private final static String DERIVED = "derived";
    private String createEntity;
    private String mode;

    public ReplaceAction(String killEntity, String actionType, String createEntity, String mode) {
        //in this action, kill is the main entity that's in the abstractAction class
        super(killEntity, actionType);
        this.createEntity = createEntity;
        this.mode = mode;
    }
    // ctor for secondary action
    public ReplaceAction(String killEntity, String actionType, String createEntity, String mode, int secondaryAmount, String secondaryEntityName, Condition condition, boolean isSelectedAll) {
        //in this action, kill is the main entity that's in the abstractAction class
        super(killEntity, actionType, secondaryAmount, secondaryEntityName, condition, isSelectedAll);
        this.createEntity = createEntity;
        this.mode = mode;
    }
    @Override
    public void invoke(Context context) {
        switch (mode){
            case SCRATCH:
                scratchFunction(context);
                break;
            case DERIVED:
                derivedFunction(context);
                break;
        }

        // mark the entity for delete
        context.getGrid().setPositionInGridBoard(null, context.getPrimaryEntityInstance().getPosX(),context.getPrimaryEntityInstance().getPosY());
        context.getPrimaryEntityInstance().setShouldKill(true);
    }

    public void scratchFunction(Context context){
       context.getEntityInstanceManager().create(context.getEntityStructures().get(createEntity),context.getGrid());
    }

    public void derivedFunction(Context context){
        context.getEntityInstanceManager().createByDerived(context.getEntityStructures().get(createEntity), context.getPrimaryEntityInstance(), context.getGrid());
    }
}

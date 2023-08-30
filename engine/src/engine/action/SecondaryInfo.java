package engine.action;

import engine.action.type.condition.Condition;

public class SecondaryInfo {
    private boolean isExistSecondary;
    private boolean isSelectedAll;
    private int amountEntities;
    private String secondaryEntityName;

    private Condition condition;

    // constructor for True scenario
    public SecondaryInfo(int amountEntities, String secondaryEntityName, Condition condition, boolean isSelectedAll) {
        this.isExistSecondary = true;
        this.amountEntities = amountEntities;
        this.secondaryEntityName = secondaryEntityName;
        this.condition = condition;
        this.isSelectedAll = isSelectedAll;
    }

    // constructor for False scenario
    public SecondaryInfo() {
        this.isExistSecondary = false;
        this.isSelectedAll = false;
        this.amountEntities = 0;
        this.secondaryEntityName = "";
        this.condition = null;
    }

    // setter
    public void setExistSecondary(boolean existSecondary) {
        isExistSecondary = existSecondary;
    }
    public void setAmountEntities(int amountEntities) {
        this.amountEntities = amountEntities;
    }
    public void setSecondaryEntityName(String secondaryEntityName) {
        this.secondaryEntityName = secondaryEntityName;
    }


    // getter
    public boolean isExistSecondary() {
        return isExistSecondary;
    }
    public int getAmountEntities() {
        return amountEntities;
    }
    public String getSecondaryEntityName() {
        return secondaryEntityName;
    }
}

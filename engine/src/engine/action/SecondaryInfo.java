package engine.action;

public class SecondaryInfo {
    private boolean isExistSecondary;
    private int amountEntities;
    private String secondaryEntityName;

    // constructor for True scenario
    public SecondaryInfo(int amountEntities, String secondaryEntityName) {
        this.isExistSecondary = true;
        this.amountEntities = amountEntities;
        this.secondaryEntityName = secondaryEntityName;
    }

    // constructor for False scenario
    public SecondaryInfo() {
        this.isExistSecondary = false;
        this.amountEntities = 0;
        this.secondaryEntityName = "";
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

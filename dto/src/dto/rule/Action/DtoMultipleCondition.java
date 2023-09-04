package dto.rule.Action;

public class DtoMultipleCondition extends DtoAbstractAction{
private String logic;
private int conditionNumber;

    public DtoMultipleCondition(String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity, String logic, int conditionNumber) {
        super(type, primaryEntity, isSecondaryExist, secondaryEntity);
        this.logic = logic;
        this.conditionNumber = conditionNumber;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public int getConditionNumber() {
        return conditionNumber;
    }

    public void setConditionNumber(int conditionNumber) {
        this.conditionNumber = conditionNumber;
    }
}

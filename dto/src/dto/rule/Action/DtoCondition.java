package dto.rule.Action;

import java.util.PrimitiveIterator;

public class DtoCondition extends DtoAbstractAction{
    private String singularity;
    private DtoMultipleCondition dtoMultipleCondition;
    private DtoSingleCondition dtoSingleCondition;

    private int thenConditionsNumber;
    private int elseConditionsNumber;

    // ctor for multiple condition
    public DtoCondition(String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity,  DtoMultipleCondition dtoMultipleCondition,
                         int thenConditionsNumber, int elseConditionsNumber) {
        super(type, primaryEntity, isSecondaryExist, secondaryEntity);
        this.singularity = "multiple";
        this.dtoMultipleCondition = dtoMultipleCondition;
        this.dtoSingleCondition = null;
        this.thenConditionsNumber = thenConditionsNumber;
        this.elseConditionsNumber = elseConditionsNumber;
    }


    // ctor for single condition
    public DtoCondition(String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity,
                         DtoSingleCondition dtoSingleCondition, int thenConditionsNumber, int elseConditionsNumber) {
        super(type, primaryEntity, isSecondaryExist, secondaryEntity);
        this.singularity = "single";
        this.dtoMultipleCondition = null;
        this.dtoSingleCondition = dtoSingleCondition;
        this.thenConditionsNumber = thenConditionsNumber;
        this.elseConditionsNumber = elseConditionsNumber;
    }

    public String getSingularity() {
        return singularity;
    }

    public void setSingularity(String singularity) {
        this.singularity = singularity;
    }

    public DtoMultipleCondition getDtoMultipleCondition() {
        return dtoMultipleCondition;
    }

    public void setDtoMultipleCondition(DtoMultipleCondition dtoMultipleCondition) {
        this.dtoMultipleCondition = dtoMultipleCondition;
    }

    public DtoSingleCondition getDtoSingleCondition() {
        return dtoSingleCondition;
    }

    public void setDtoSingleCondition(DtoSingleCondition dtoSingleCondition) {
        this.dtoSingleCondition = dtoSingleCondition;
    }

    public int getThenConditionsNumber() {
        return thenConditionsNumber;
    }

    public void setThenConditionsNumber(int thenConditionsNumber) {
        this.thenConditionsNumber = thenConditionsNumber;
    }

    public int getWhenConditionsNumber() {
        return elseConditionsNumber;
    }

    public void setWhenConditionsNumber(int elseConditionsNumber) {
        this.elseConditionsNumber = elseConditionsNumber;
    }
}

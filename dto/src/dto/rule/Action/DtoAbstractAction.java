package dto.rule.Action;

public class DtoAbstractAction {
    private String type;
    private String primaryEntity;
    private boolean isSecondaryExist;
    private String secondaryEntity;

    // Calculation
    private String resultProp;
    private String arg1;
    private String arg2;
    private String operatorType;

    // condition
    private String singularity;
    private DtoMultipleCondition dtoMultipleCondition;
    private DtoSingleCondition dtoSingleCondition;
    private int thenConditionsNumber;
    private int elseConditionsNumber;

    // Decrease/ Increase
    private String property;
    private String byExpression;

    // Proximity
    private String targetEntity;
    private String envDepthOf;

    // Replace
    private String createEntity;
    private String mode;

    // Set
   // private String property;
    private String newValue;


    public DtoAbstractAction() {}
    public DtoAbstractAction(String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity) {
        this.type = type;
        this.primaryEntity = primaryEntity;
        this.isSecondaryExist = isSecondaryExist;
        this.secondaryEntity = secondaryEntity;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPrimaryEntity() {
        return primaryEntity;
    }
    public void setPrimaryEntity(String primaryEntity) {
        this.primaryEntity = primaryEntity;
    }
    public boolean isSecondaryExist() {
        return isSecondaryExist;
    }
    public void setSecondaryExist(boolean secondaryExist) {
        isSecondaryExist = secondaryExist;
    }
    public String getSecondaryEntity() {
        return secondaryEntity;
    }
    public void setSecondaryEntity(String secondaryEntity) {
        this.secondaryEntity = secondaryEntity;
    }


    // setters of all properties for the spesific actions

    public void setResultProp(String resultProp) {
        this.resultProp = resultProp;
    }
    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }
    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }
    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }
    public void setSingularity(String singularity) {
        this.singularity = singularity;
    }
    public void setDtoMultipleCondition(DtoMultipleCondition dtoMultipleCondition) {
        this.dtoMultipleCondition = dtoMultipleCondition;
    }
    public void setDtoSingleCondition(DtoSingleCondition dtoSingleCondition) {
        this.dtoSingleCondition = dtoSingleCondition;
    }
    public void setProperty(String property) {
        this.property = property;
    }
    public void setByExpression(String byExpression) {
        this.byExpression = byExpression;
    }
    public void setTargetEntity(String targetEntity) {
        this.targetEntity = targetEntity;
    }
    public void setEnvDepthOf(String envDepthOf) {
        this.envDepthOf = envDepthOf;
    }
    public void setCreateEntity(String createEntity) {
        this.createEntity = createEntity;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public void setThenConditionsNumber(int thenConditionsNumber) {
        this.thenConditionsNumber = thenConditionsNumber;
    }

    public void setElseConditionsNumber(int elseConditionsNumber) {
        this.elseConditionsNumber = elseConditionsNumber;
    }


    public String getResultProp() {
        return resultProp;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public String getSingularity() {
        return singularity;
    }

    public DtoMultipleCondition getDtoMultipleCondition() {
        return dtoMultipleCondition;
    }

    public DtoSingleCondition getDtoSingleCondition() {
        return dtoSingleCondition;
    }

    public int getThenConditionsNumber() {
        return thenConditionsNumber;
    }

    public int getElseConditionsNumber() {
        return elseConditionsNumber;
    }

    public String getProperty() {
        return property;
    }

    public String getByExpression() {
        return byExpression;
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public String getEnvDepthOf() {
        return envDepthOf;
    }

    public String getCreateEntity() {
        return createEntity;
    }

    public String getMode() {
        return mode;
    }

    public String getNewValue() {
        return newValue;
    }
}

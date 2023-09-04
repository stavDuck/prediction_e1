package dto.rule.Action;

public class DtoSingleCondition extends DtoAbstractAction{
    private String property;
    private String operator;
    private String value;

    public DtoSingleCondition(String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity, String property, String operator, String value) {
        super(type, primaryEntity, isSecondaryExist, secondaryEntity);
        this.property = property;
        this.operator = operator;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

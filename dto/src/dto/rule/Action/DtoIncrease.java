package dto.rule.Action;

public class DtoIncrease extends DtoAbstractAction{
    private String property;
    private String byExpression;

    public DtoIncrease(String property, String byExpression, String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity) {
        super(type, primaryEntity, isSecondaryExist, secondaryEntity);
        this.property = property;
        this.byExpression = byExpression;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getByExpression() {
        return byExpression;
    }

    public void setByExpression(String byExpression) {
        this.byExpression = byExpression;
    }
}

package dto.rule.Action;

public class DtoSet extends DtoAbstractAction {
    private String property;
    private String newValue;

    public DtoSet(String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity, String property, String newValue) {
        super(type, primaryEntity, isSecondaryExist, secondaryEntity);
        this.property = property;
        this.newValue = newValue;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}

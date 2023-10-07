package dto.rule.Action;

public class DtoAbstractAction {
    private String type;
    private String primaryEntity;
    private boolean isSecondaryExist;
    private String secondaryEntity;

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
}

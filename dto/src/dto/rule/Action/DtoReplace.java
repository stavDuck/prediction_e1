package dto.rule.Action;

public class DtoReplace extends DtoAbstractAction{
    private String createEntity;
    private String mode;

    public DtoReplace(String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity, String createEntity, String mode) {
        super(type, primaryEntity, isSecondaryExist, secondaryEntity);
        this.createEntity = createEntity;
        this.mode = mode;
    }

    public void setCreateEntity(String createEntity) {
        this.createEntity = createEntity;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}

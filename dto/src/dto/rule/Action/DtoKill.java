package dto.rule.Action;

public class DtoKill extends DtoAbstractAction{
    public DtoKill(String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity) {
        super(type, primaryEntity, isSecondaryExist, secondaryEntity);
    }
}

package dto.rule.Action;

public class DtoProximity extends DtoAbstractAction{
    private String targetEntity;
    private String envDepthOf;

    public DtoProximity(String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity, String targetEntity, String envDepthOf) {
        super(type, primaryEntity, isSecondaryExist, secondaryEntity);
        this.targetEntity = targetEntity;
        this.envDepthOf = envDepthOf;
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(String targetEntity) {
        this.targetEntity = targetEntity;
    }

    public String getEnvDepthOf() {
        return envDepthOf;
    }

    public void setEnvDepthOf(String envDepthOf) {
        this.envDepthOf = envDepthOf;
    }
}

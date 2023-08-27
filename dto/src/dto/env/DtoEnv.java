package dto.env;

import dto.range.DtoRange;

public class DtoEnv {
    private String envType;
    private String envName;
    private DtoRange envRange;

    public DtoEnv(String envType, String envName, float from, float to) {
        this.envType = envType;
        this.envName = envName;
        this.envRange = new DtoRange(to,from);
    }
    public DtoEnv(String envType, String envName, DtoRange range) {
        this.envType = envType;
        this.envName = envName;
        this.envRange = range;
    }

    public String getEnvType() {
        return envType;
    }

    public void setEnvType(String envType) {
        this.envType = envType;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }
}

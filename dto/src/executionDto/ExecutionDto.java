package executionDto;

import java.util.HashMap;
import java.util.Map;

public class ExecutionDto {
    private String fileName;
    private Map<String, Integer> populationMap;
    private Map<String, String> environmantMap;

    public ExecutionDto() {
        this.populationMap = new HashMap<>();
        this.environmantMap = new HashMap<>();
    }

    public ExecutionDto(String fileName) {
        this.fileName = fileName;
        this.populationMap = new HashMap<>();
        this.environmantMap = new HashMap<>();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public void addEntityPopulation(String entityName, int population) {
        populationMap.put(entityName, population);
    }

    public void addEnvVariable(String envName, String value) {
        environmantMap.put(envName, value);
    }

    public Map<String, Integer> getPopulationMap() {
        return populationMap;
    }

    public Map<String, String> getEnvironmantMap() {
        return environmantMap;
    }

    public String getFileName() {
        return fileName;
    }
}

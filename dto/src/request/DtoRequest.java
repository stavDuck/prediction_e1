package request;

import dto.termination.DtoTermination;

public class DtoRequest {
    private Integer requestId;
    private String simulationXmlName;
    private Integer simulationRequstedRuns;
    private Integer simulationCurrentRunning;
    private Integer simulationLeftoverRuns;
    // if termination is null - condition is stop by user
    private DtoTermination terminationConditions;
    private String status;
    private String userName;

    public DtoRequest(Integer requestId, String simulationXmlName, Integer simulationRequstedRuns, Integer simulationCurrentRunning, Integer simulationLeftoverRuns, DtoTermination terminationConditions, String status, String userName) {
        this.requestId = requestId;
        this.simulationXmlName = simulationXmlName;
        this.simulationRequstedRuns = simulationRequstedRuns;
        this.simulationCurrentRunning = simulationCurrentRunning;
        this.simulationLeftoverRuns = simulationLeftoverRuns;
        this.terminationConditions = terminationConditions;
        this.status = status;
        this.userName = userName;
    }

    // setters
    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }
    public void setSimulationXmlName(String simulationXmlName) {
        this.simulationXmlName = simulationXmlName;
    }
    public void setSimulationRequstedRuns(Integer simulationRequstedRuns) {
        this.simulationRequstedRuns = simulationRequstedRuns;
    }
    public void setSimulationCurrentRunning(Integer simulationCurrentRunning) {
        this.simulationCurrentRunning = simulationCurrentRunning;
    }
    public void setSimulationLeftoverRuns(Integer simulationLeftoverRuns) {
        this.simulationLeftoverRuns = simulationLeftoverRuns;
    }
    public void setTerminationConditions(DtoTermination terminationConditions) {
        this.terminationConditions = terminationConditions;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // getters
    public Integer getRequestId() {
        return requestId;
    }
    public String getSimulationXmlName() {
        return simulationXmlName;
    }
    public Integer getSimulationRequstedRuns() {
        return simulationRequstedRuns;
    }
    public Integer getSimulationCurrentRunning() {
        return simulationCurrentRunning;
    }
    public Integer getSimulationLeftoverRuns() {
        return simulationLeftoverRuns;
    }
    public DtoTermination getTerminationConditions() {
        return terminationConditions;
    }
    public String getStatus() {
        return status;
    }
    public String getUserName() {
        return userName;
    }
}

package request;

import dto.termination.DtoTermination;

public class DtoRequest {
    private Integer requestId;
    private String simulationXmlName;
    private Integer simulationRequestedRuns;
    private Integer simulationCurrentRunning;
    private Integer simulationLeftoverRuns;
    private Integer simulationFinishedRuns;
    // if termination is null - condition is stop by user
    private DtoTermination terminationConditions;
    private String status;
    private String userName;

    public DtoRequest(Integer requestId, String simulationXmlName, Integer simulationRequestedRuns, Integer simulationCurrentRunning, Integer simulationLeftoverRuns,
                      Integer simulationFinishedRuns, DtoTermination terminationConditions, String status, String userName) {
        this.requestId = requestId;
        this.simulationXmlName = simulationXmlName;
        this.simulationRequestedRuns = simulationRequestedRuns;
        this.simulationCurrentRunning = simulationCurrentRunning;
        this.simulationLeftoverRuns = simulationLeftoverRuns;
        this.simulationFinishedRuns = simulationFinishedRuns;
        this.terminationConditions = terminationConditions;
        this.status = status;
        this.userName = userName;
    }

    // setters

    public void setSimulationFinishedRuns(Integer simulationFinishedRuns) {
        this.simulationFinishedRuns = simulationFinishedRuns;
    }
    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }
    public void setSimulationXmlName(String simulationXmlName) {
        this.simulationXmlName = simulationXmlName;
    }
    public void setSimulationRequestedRuns(Integer simulationRequestedRuns) {
        this.simulationRequestedRuns = simulationRequestedRuns;
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

    public Integer getSimulationFinishedRuns() {
        return simulationFinishedRuns;
    }
    public Integer getRequestId() {
        return requestId;
    }
    public String getSimulationXmlName() {
        return simulationXmlName;
    }
    public Integer getSimulationRequestedRuns() {
        return simulationRequestedRuns;
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

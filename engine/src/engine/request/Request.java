package engine.request;
import dto.termination.DtoTermination;
import engine.termination.Termination;
import request.DtoRequest;

public class Request {
    private static int idGenerator = 1; // generate id for every new request
    private static final String PENDING_STATUS = "pending";
    private static final String APPROVED_STATUS = "approved";
    private static final String DENIED_STATUS = "denied";
    private int requestId;
    private String simulationXmlName;
    private int simulationRequestedRuns;
    private int simulationCurrentRunning;
    private int simulationLeftoverRuns;
    private Termination terminationConditions;
    private String status;
    private String userName;

    public Request(String simulationXmlName, int simulationRequestedRuns, Termination terminationConditions, String userName) {
        this.requestId = idGenerator;
        idGenerator ++;

        this.simulationXmlName = simulationXmlName;
        this.simulationRequestedRuns = simulationRequestedRuns;
        this.simulationCurrentRunning = 0; // default value
        this.simulationLeftoverRuns = simulationRequestedRuns; // default value
        this.terminationConditions = terminationConditions;
        this.status = PENDING_STATUS;
        this.userName = userName;
    }

    // setters
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
    public void setSimulationXmlName(String simulationXmlName) {
        this.simulationXmlName = simulationXmlName;
    }
    public void setSimulationRequestedRuns(int simulationRequestedRuns) {
        this.simulationRequestedRuns = simulationRequestedRuns;
    }
    public void setSimulationCurrentRunning(int simulationCurrentRunning) {
        this.simulationCurrentRunning = simulationCurrentRunning;
    }
    public void setSimulationLeftoverRuns(int simulationLeftoverRuns) {
        this.simulationLeftoverRuns = simulationLeftoverRuns;
    }
    public void setTerminationConditions(Termination terminationConditions) {
        this.terminationConditions = terminationConditions;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // getters
    public int getRequestId() {
        return requestId;
    }
    public String getSimulationXmlName() {
        return simulationXmlName;
    }
    public int getSimulationRequestedRuns() {
        return simulationRequestedRuns;
    }
    public int getSimulationCurrentRunning() {
        return simulationCurrentRunning;
    }
    public int getSimulationLeftoverRuns() {
        return simulationLeftoverRuns;
    }
    public Termination getTerminationConditions() {
        return terminationConditions;
    }
    public String getStatus() {
        return status;
    }
    public String getUserName() {
        return userName;
    }

    public DtoRequest createDtoRequest() {
        DtoTermination dtoTermination = new DtoTermination(terminationConditions.getByTick(), terminationConditions.getBySec());

        return new DtoRequest(requestId, simulationXmlName, simulationRequestedRuns, simulationCurrentRunning, simulationLeftoverRuns,
                dtoTermination, status, userName);
    }
}

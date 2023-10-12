package engine.request;
import engine.termination.Termination;
public class Request {
    private static final String PENDING_STATUS = "pending";
    private static final String APPROVED_STATUS = "approved";
    private static final String DENIED_STATUS = "denied";
    private int requestId;
    private String simulationXmlName;
    private int simulationRequstedRuns;
    private int simulationCurrentRunning;
    private int simulationLevtoverRuns;
    private Termination terminationConditions;
    private String status;
    private String userName;

    public Request(int requestId, String simulationXmlName, int simulationRequstedRuns, int simulationCurrentRunning, int simulationLevtoverRuns, Termination terminationConditions, String userName) {
        this.requestId = requestId;
        this.simulationXmlName = simulationXmlName;
        this.simulationRequstedRuns = simulationRequstedRuns;
        this.simulationCurrentRunning = simulationCurrentRunning;
        this.simulationLevtoverRuns = simulationLevtoverRuns;
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
    public void setSimulationRequstedRuns(int simulationRequstedRuns) {
        this.simulationRequstedRuns = simulationRequstedRuns;
    }
    public void setSimulationCurrentRunning(int simulationCurrentRunning) {
        this.simulationCurrentRunning = simulationCurrentRunning;
    }
    public void setSimulationLevtoverRuns(int simulationLevtoverRuns) {
        this.simulationLevtoverRuns = simulationLevtoverRuns;
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
    public int getSimulationRequstedRuns() {
        return simulationRequstedRuns;
    }
    public int getSimulationCurrentRunning() {
        return simulationCurrentRunning;
    }
    public int getSimulationLevtoverRuns() {
        return simulationLevtoverRuns;
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
}

package engine.simulation;
import java.util.HashMap;
import java.util.Map;

// This class will be handaling multiple xmls, every xml will be under a simulation object
// simulations will be saved accodring to the id of the simulation
public class SimulationMultipleManager {
        private Map<String, Simulation> simulationsManager;

    public SimulationMultipleManager() {
        this.simulationsManager = new HashMap<>();
    }

    public Map<String, Simulation> getSimulationsManager() {
        return simulationsManager;
    }

    public void setSimulationsManager(Map<String, Simulation> simulationsManager) {
        this.simulationsManager = simulationsManager;
    }

    public void addSimulationToSimulationMultipleManager(String name,Simulation simulation){
        simulationsManager.put(name, simulation);
    }

    // return true if uniq name already exist, else return false
    public boolean isNameExistInMap(String name){
        return (simulationsManager.get(name) != null);
    }

    public void removeSimulationFromSimulationMultipleManager(String name){
        simulationsManager.remove(name);
    }
}

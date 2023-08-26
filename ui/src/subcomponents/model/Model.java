package subcomponents.model;

import engine.simulation.Simulation;
import engine.simulation.SimulationHistory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private static int idGenerator = 1;
    private Simulation simulation;
    private List<SimulationHistory> simulationHistory;
    private boolean isSimulationDone = false;
    private boolean isSimulateHistoryNotEmpty = false;
    private boolean isFileLoaded = false;

    // function gets file name, try to load new simulation, if successful return "" else - return the error information
    public String loadXmlFile(String fileName){
        Simulation tempSimlate = null;
        LocalDate currentDate;
        LocalTime currentTime;
        // list of simulations history
        simulationHistory = null;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH.mm.ss");

        try {
            tempSimlate = createSimulationFromFile(fileName);
            tempSimlate.setSimulationID(idGenerator);
            idGenerator++;

            // if all went good
            isFileLoaded = true;
            simulation = tempSimlate;

            simulationHistory = new ArrayList<>(); // every time we load new XML the history is deleted
            isSimulateHistoryNotEmpty = false;
        } catch (RuntimeException e) {
            return (e.getMessage() +
                    "\nplease try to fix the issue and reload the xml again.\n");
        }

        // loaded sucessfuly
        return "";
    }

    public Simulation createSimulationFromFile(String fileName) throws RuntimeException {
        Simulation simulation= new Simulation(fileName);
        return simulation;
    }


}

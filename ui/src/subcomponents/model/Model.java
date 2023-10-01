package subcomponents.model;

import dto.Dto;
import engine.simulation.Simulation;
import engine.simulation.SimulationHistory;
import engine.simulation.execution.SimulationExecution;
import engine.simulation.execution.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private Simulation simulation;
    private boolean isFileLoaded = false;
    private int currSimulationId;

    // function gets file name, try to load new simulation, if successful return "" else - return the error information
    public String loadXmlFile(String fileName){
        Simulation tempSimulation = null;
        int tempSimulationID;

        LocalDate currentDate;
        LocalTime currentTime;
        // list of simulations history
       // simulationHistory = null;
        //DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        //DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH.mm.ss");

        try {
            tempSimulation = new Simulation();
            // reset uniq id to 1 every time we laod new xml
            tempSimulation.resetUniqID();
            tempSimulationID = tempSimulation.createSimulation(fileName);
            isFileLoaded = true;

            //tempSimulation = createSimulationFromFile(fileName);
           // tempSimlate.setSimulationID(idGenerator);
            //idGenerator++;

            // if all went good
            simulation = tempSimulation;
            currSimulationId = tempSimulationID;

            //simulationHistory = new ArrayList<>(); // every time we load new XML the history is deleted
            //isSimulateHistoryNotEmpty = false;
        } catch (RuntimeException e) {
            return (e.getMessage() +
                    "\nplease try to fix the issue and reload the xml again.\n");
        }

        // loaded sucessfuly
        return "File loaded successfully";
    }

    /*public Simulation createSimulationFromFile(String fileName) throws RuntimeException {
        Simulation simulation= new Simulation(fileName);
        return simulation;
    }*/

    public Dto getDtoWorld(){
        return simulation.getSimulationById(currSimulationId).getWorld().createDto();
       // return simulation.getWorld().createDto();
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void runSimulation() {
        try {
            LocalDate currentDate;
            LocalTime currentTime;
            // simulation already ran need to create a new one
            /*if(simulation.getWorld().getTermination().isStop() == true){
                simulation = loadFileXML(fileName);
                simulation.setSimulationID(idGenerator);
                idGenerator++;
            }*/

            // set env values
            //setSimulationEnvValues(simulation);
            // Print all env names + values
            //printEnvLivsNamesAndValues(simulation);
            currentDate = LocalDate.now();
            currentTime = LocalTime.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH.mm.ss");

            // set startSimulation in simulation history
            //simulationHistory.add(new SimulationHistory(simulation, simulation.getSimulationID(),
                    //currentDate.format(dateFormatter), currentTime.format(timeFormatter)));
           // isSimulateHistoryNotEmpty = true;

            // run simulation
           // simulation.run();
            simulation.execute(currSimulationId);

           // isSimulationDone = true;
            // set the end simulation after run is done
            //simulationHistory.get(simulationHistory.size() - 1).setEndSimulation(simulation);
        }
        catch (RuntimeException e) {
            //System.out.println("Simulation failed with an error: " + e.getMessage());
        }
    }

   // public boolean getSimulationDone() {
    //    return isSimulationDone;
   // }

    public SimulationExecution getSimulationById(int id) {
        return simulation.getSimulationById(id);
        //return simulationHistory.get(id).getStartSimulation();
    }

    public SimulationExecution getCurrSimulation(){
        return simulation.getSimulationById(currSimulationId);
    }

    public int getCurrSimulationId() {
        return currSimulationId;
    }

    public void setCurrSimulationId(int currSimulationId) {
        this.currSimulationId = currSimulationId;
    }
}


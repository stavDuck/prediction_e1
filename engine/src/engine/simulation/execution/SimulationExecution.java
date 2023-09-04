package engine.simulation.execution;

import dto.Dto;
import engine.property.PropertyInstance;
import engine.simulation.copyhandler.CopyHandler;
import engine.validation.WorldValidator;
import engine.validation.exceptions.XmlValidationException;
import engine.world.World;
import generated.PRDWorld;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SimulationExecution {
    private static final String JAXB_XML_GAME_PACKAGE_NAME = "generated";
    private World world;
    private  int id;
    private Status simulationStatus;


    // set World information
    public SimulationExecution(String fileName) throws RuntimeException{
        try {
            //String absolutePath = new File(fileName).getAbsolutePath();
            InputStream inputStream = new FileInputStream(new File(fileName));

            // creating PRDWorld
            PRDWorld prdWorld = deserializeFrom(inputStream);
            // validating PRDworld XML
            WorldValidator worldValidator = new WorldValidator();
            worldValidator.validateWorldData(prdWorld);

            // copy the information from PRDWorld to World
            world = new World();
            CopyHandler copy = new CopyHandler();
            copy.copyData(prdWorld, world);

            simulationStatus = Status.CREATED;
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("File " + fileName + " was not found");
        }
        catch (JAXBException e){
            throw new RuntimeException("File " + fileName + " JAXB upload filed, please check the xml");
        }
        // exceptions from validate world
        catch (XmlValidationException e){
            throw new RuntimeException("xml validation failed with the error: \n" + e.getMessage());
        }
    }
    private PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }

    // function 3 run simulation
    public void run() {
        // create all instances
        world.createEntitiesInstances();
        try {
            // run all rules
            world.invokeRules();
        }
        catch (IllegalArgumentException e) {}
    }

    // getters
    public World getWorld() {
        return world;
    }
    public int getId() {
        return id;
    }
    public Status getSimulationStatus() {
        return simulationStatus;
    }

    // setters
    public void setWorld(World world) {
        this.world = world;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setSimulationStatus(Status simulationStatus) {
        this.simulationStatus = simulationStatus;
    }

    public void setPopulationForEntity(String entityName, int population) {
        world.setPopulationForEntity(entityName, population);
    }

    public void setEnvVariable(String envVariableName, String value) throws NumberFormatException {
        world.setEnvValueByName(envVariableName, value);
    }

    public PropertyInstance getEnvPropertyInstance(String envPropName) {
        return world.getEnvironment().getEnvProperty(envPropName);
    }

    public Dto createWorldDto() {
        return world.createDto();
    }

    public void pauseSimulation(){
        simulationStatus = Status.PAUSE;
        world.pause();
    }

    public void stopSimulation() {
        if (simulationStatus == Status.PAUSE) {
            world.resume();
        }
        simulationStatus = Status.STOP;
        world.stopByUser();
    }


    public void resume() {
            simulationStatus = Status.IN_PROGRESS;
            world.resume();
    }
}

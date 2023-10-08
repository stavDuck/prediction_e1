package engine.simulation.execution;

import com.sun.corba.se.spi.ior.ObjectKey;
import dto.Dto;
import dto.entity.DtoEntity;
import engine.entity.EntityInstance;
import engine.property.PropertyInstance;
import engine.property.tickhistory.TickHistory;
import engine.property.type.Type;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimulationExecution {
    private static final String JAXB_XML_GAME_PACKAGE_NAME = "generated";
    private World world;
    private  int id;
    private String userName;
    private Status simulationStatus;
    private boolean isSimulationSelected;

    // NEW !!!
    // set World information
    public SimulationExecution(InputStream inputStream) throws RuntimeException {
        this(inputStream, "");
    }
    public SimulationExecution(InputStream inputStream, String userName) throws RuntimeException{
        isSimulationSelected = false;
        try {
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
            this.userName = userName; // default for first time Admin uploading iformation
        }
        catch (JAXBException e){
            //throw new RuntimeException("File " + fileName + " JAXB upload filed, please check the xml");
            throw new RuntimeException("JAXB upload failed, please check the xml");
        }
        // exceptions from validate world
        catch (XmlValidationException e){
            throw new RuntimeException("xml validation failed with the error: \n" + e.getMessage());
        }
    }



    /*public SimulationExecution(String fileName) throws RuntimeException{
        isSimulationSelected = false;
        try {
            String absolutePath = new File(fileName).getAbsolutePath();
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
            //throw new RuntimeException("File " + fileName + " JAXB upload filed, please check the xml");
            throw new RuntimeException("JAXB upload failed, please check the xml");
        }
        // exceptions from validate world
        catch (XmlValidationException e){
            throw new RuntimeException("xml validation failed with the error: \n" + e.getMessage());
        }
    }*/




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
        catch (IllegalArgumentException e) {} catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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

    public int getPopulationForEntity(String entityName) {
        return world.getPopulationForEntity(entityName);
    }


    public void setEnvVariable(String envVariableName, String value) throws NumberFormatException {
        world.setEnvValueByName(envVariableName, value);
    }

    public void setEnvOriginalVariable(String envVariableName, String value) throws NumberFormatException {
        world.setEnvOriginalValueByName(envVariableName, value);
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

    public long getRunningSeconds() {
        return world.getRunningTime();
    }

    public boolean isSimulationSelected() {
        return isSimulationSelected;
    }

    public void setSimulationSelected(boolean simulationSelected) {
        isSimulationSelected = simulationSelected;
    }

    // used in screen 3 for histogram
    public List<Map.Entry<Object, Long>> getHistogramByEntityAndValue(String entityName, String propertyName) {
        Map<Object, Long> countByPropertyValue = world.getInstanceManager().getInstancesByName(entityName).stream()
                .collect(Collectors.groupingBy(
                        instance -> instance.getPropertyInstanceByName(propertyName).getVal(),
                        Collectors.counting()));

        List<Map.Entry<Object, Long>> sortedEntries = countByPropertyValue.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
        return sortedEntries;
    }

    public String getErrorStopSimulation(){
        return world.getErrorStopSimulation();
    }

    public String averageChangesInProperty(String entityName, String propertyName) {
        double totalSum = 0.0;
        double tickHistorySum = 0.0;
        for(EntityInstance entityInstance : world.getInstanceManager().getInstancesByName(entityName)) {
            PropertyInstance propertyInstance = entityInstance.getPropertyInstanceByName(propertyName);
            for(TickHistory tickHistory : propertyInstance.getTickHistory()) {
                tickHistorySum += (tickHistory.getEndTick() - tickHistory.getStartTick());
            }
            if(!propertyInstance.getTickHistory().isEmpty())
                totalSum += (tickHistorySum / propertyInstance.getTickHistory().size());
            tickHistorySum = 0.0;
        }
        if(totalSum == 0.0) {
            String text = "Property " + propertyName + " value hasn't changed.";
            return text;
        }

        else if(!world.getInstanceManager().getInstancesByName(entityName).isEmpty()) {
            return String.valueOf((totalSum / world.getInstanceManager().getInstancesByName(entityName).size()));
        }
        else {
            String text = "All " + entityName + " entities are dead, no data to present.";
            return text;
        }
    }

    public String averagePropertyValue(String entityName, String propertyName) {
        Float sum = 0.0f;

        List<EntityInstance> entityInstances = world.getInstanceManager().getInstancesByName(entityName);

        for (EntityInstance entityInstance : entityInstances) {
            if(entityInstance.getPropertyInstanceByName(propertyName).getType() != Type.FLOAT &&
                    entityInstance.getPropertyInstanceByName(propertyName).getType() != Type.DECIMAL) {
                String text = "Property " + propertyName + " Type is not numeric, no data to present.";
                return text;
            }
            sum += (Float) entityInstance.getPropertyInstanceByName(propertyName).getVal();
        }

        if(entityInstances.isEmpty()) {
            String text = "All " + entityName + " entities are dead, no data to present.";
            return text;
        }
        else {
            return String.format("%.2f", sum / entityInstances.size());
        }

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getXmlName(){
        return world.getName();
    }
}

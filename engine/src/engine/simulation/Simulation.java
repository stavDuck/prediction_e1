package engine.simulation;
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

public class Simulation {
    private static final String JAXB_XML_GAME_PACKAGE_NAME = "generated";
    private World world;
    private  int simulationID;

    public Simulation(String fileName) throws RuntimeException{
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
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("File " + fileName + " was not found");
        }
        catch (JAXBException e){
            throw new RuntimeException("File " + fileName + " JAXB upload filed, please check the xml");
        }
        // exceptions from validate world
        catch (XmlValidationException e){
            throw new RuntimeException("xml validation failed with the error: " + e.getMessage());
        }
    }
    private PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }


    public World getWorld(){
        return world;
    }
    public int getSimulationID(){return simulationID;}
    public void setSimulationID(int i){simulationID = i;}

    public void run() {
        // create all instances
        world.createEntitiesInstances();
        try {
            // run all rules
            world.invokeRules();
        }
        catch (IllegalArgumentException e) {}
    }
}

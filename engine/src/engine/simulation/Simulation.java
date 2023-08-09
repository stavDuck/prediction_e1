package engine.simulation;

import engine.simulation.copyhandler.CopyHandler;
import engine.validation.WorldValidator;
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

    public Simulation(String fileName) {
        try {
            //String absolutePath = new File(fileName).getAbsolutePath();
            InputStream inputStream = new FileInputStream(new File(fileName));
            PRDWorld prdWorld = deserializeFrom(inputStream);
            WorldValidator.validateWorldData(prdWorld);

            world = new World();
            CopyHandler.copyData(prdWorld, world);
        }
        catch (JAXBException | FileNotFoundException e) {
            System.out.println("File " + fileName + " was not found");
        }
    }
    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }



}

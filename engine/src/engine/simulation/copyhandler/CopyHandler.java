package engine.simulation.copyhandler;

import engine.entity.EntityStructure;
import engine.range.Range;
import engine.termination.Termination;
import engine.world.World;
import generated.*;

import java.util.List;

public class CopyHandler {
    public static void copyData(PRDWorld prdWorld, World world) {
        copyEnvironmentProperties(prdWorld, world);
        copyEntityStructure(prdWorld, world);
        //copy rules
        copyTermination(prdWorld, world);
    }

    public static void copyEnvironmentProperties(PRDWorld prdWorld, World world) {
        List<PRDEnvProperty> prdList = prdWorld.getPRDEvironment().getPRDEnvProperty();
        for(PRDEnvProperty property : prdList) {

            // if range in null (when type is not decimal/ float) set range in our world as null
            world.getEnvironment().addEnvProperty(property.getPRDName(),
                    property.getType(),
                    (property.getPRDRange() == null ? null :
                    new Range((int)property.getPRDRange().getTo(), (int)property.getPRDRange().getFrom())),
                    null);
        }
    }

    public static void copyEntityStructure(PRDWorld prdWorld, World world) {
        List<PRDEntity> prdEntityListList = prdWorld.getPRDEntities().getPRDEntity();
        for(PRDEntity entity : prdEntityListList) {
            //adding a new entity to the world's entityStructure map
            world.addEntityStructure(entity.getName(), new EntityStructure(entity.getPRDPopulation(), entity.getName()));

            List<PRDProperty> prdPropertyList = entity.getPRDProperties().getPRDProperty();
            for(PRDProperty property : prdPropertyList) {
                //go to the created entity, and add all property structures
                world.getEntityStructures().get(entity.getName()).addProperty(property.getPRDName(),
                        property.getType(),
                        new Range((int)property.getPRDRange().getTo(), (int)property.getPRDRange().getFrom()),
                        property.getPRDValue().isRandomInitialize(),
                        property.getPRDValue().getInit());
            }

        }

    }


    public static void copyTermination(PRDWorld prdWorld, World world) {
        PRDByTicks ticks = (PRDByTicks) prdWorld.getPRDTermination().getPRDByTicksOrPRDBySecond().get(0);
        PRDBySecond seconds = (PRDBySecond) prdWorld.getPRDTermination().getPRDByTicksOrPRDBySecond().get(1);
        world.setTermination(new Termination(ticks.getCount(), seconds.getCount()));
    }
}

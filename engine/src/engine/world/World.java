package engine.world;

import engine.entity.Entity;
import engine.environment.Environment;
import engine.rule.Rules;
import engine.termination.Termination;

import java.util.List;

public class World {
    private Environment environment;
    List<Entity> entities;
    List<Rules> rules;
    Termination termination;

}

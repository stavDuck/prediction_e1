package engine.rule;

import engine.action.Action;
import engine.activation.Activation;

import java.util.List;

public class Rules {
    private String name;
    private List<Action> actions; //can have multiple actions in one rule
    private Activation activation;
}

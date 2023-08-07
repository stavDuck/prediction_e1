package engine.rule;

import engine.action.AbstractAction;
import engine.activation.Activation;

import java.util.List;

public class Rules {
    private String name;
    private List<AbstractAction> actions; //can have multiple actions in one rule
    private Activation activation;

    // getters
    public String getName() {
        return name;
    }
    public List<AbstractAction> getActions() {
        return actions;
    }
    public Activation getActivation() {
        return activation;
    }

    // setters

    public void setName(String name) {
        this.name = name;
    }
    public void setActivation(Activation activation) {
        this.activation = activation;
    }
}

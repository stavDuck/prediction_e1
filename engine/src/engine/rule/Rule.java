package engine.rule;

import engine.action.AbstractAction;
import engine.activation.Activation;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private String name;
    private List<AbstractAction> actions; //can have multiple actions in one rule
    private Activation activation;

    public Rule(String name, Activation activation){
        this.name = name;
        actions = new ArrayList<>();
        this.activation = activation;
    }

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

    public void printRule() {
        System.out.println("Rule name: " + name);
        System.out.println("Rule action number: " + actions.size());
        System.out.println("Rule actions: " + name);
        System.out.println("---------------------------");
        actions.forEach(value -> value.printAction());
        System.out.println("Rule activation: " + name);
        System.out.println("---------------------------");
        activation.printActivation();

    }
}

package dto.rule;

import dto.rule.activation.DtoActivation;

import java.util.ArrayList;
import java.util.List;

public class DtoRule {
    private String name;
    private int actionNumber;
    private DtoActivation activation;
    private List<String> actionNames;

    public DtoRule(String name, DtoActivation activation, int actionNumber) {
        this.name = name;
        this.activation = activation;
        this.actionNumber = actionNumber;
        this.actionNames = new ArrayList<>();
    }

    public void addAction(String actionName) {
        actionNames.add(actionName);
    }

    public void printRule() {
        System.out.println("Rule name: " + name);
        System.out.println("Rule action number: " + actionNumber);
        System.out.println("Rule actions: ");
        System.out.println("------------------------------");
        actionNames.forEach(value -> System.out.println(value.toLowerCase()));
        System.out.println("Rule activation: ");
        System.out.println("----------------");
        activation.printActivation();
        System.out.println("");
    }

}

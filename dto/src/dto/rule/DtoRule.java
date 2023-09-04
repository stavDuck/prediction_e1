package dto.rule;

import dto.rule.Action.*;
import dto.rule.activation.DtoActivation;

import java.util.ArrayList;
import java.util.List;

public class DtoRule {
    private String name;
    private int actionNumber;
    private DtoActivation activation;
    private List<DtoAbstractAction> dtoActions;

    public DtoRule(String name, DtoActivation activation, int actionNumber) {
        this.name = name;
        this.activation = activation;
        this.actionNumber = actionNumber;
        this.dtoActions = new ArrayList<>();
    }

    public void addAction(DtoAbstractAction action){
        dtoActions.add(action);
    }


    public void printRule() {
        System.out.println("Rule name: " + name);
        System.out.println("Rule action number: " + actionNumber);
        System.out.println("Rule actions: ");
        System.out.println("------------------------------");
        dtoActions.forEach(value -> System.out.println(value.getType()));
        System.out.println("Rule activation: ");
        System.out.println("----------------");
        activation.printActivation();
        System.out.println("");
    }

    public String getName() {
        return name;
    }

    public int getActionNumber() {
        return actionNumber;
    }

    public DtoActivation getActivation() {
        return activation;
    }

    public List<DtoAbstractAction> getAction() {
        return dtoActions;
    }
}

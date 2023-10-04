package subcomponents.tabs.results.logic.task.population;

import javafx.beans.property.SimpleIntegerProperty;

public class FillPopulation {
    private String entityName;
    private SimpleIntegerProperty population;


    public FillPopulation(String entityName) {
        this.entityName = entityName;
        this.population = new SimpleIntegerProperty();
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getPopulation() {
        return population.get();
    }

    public SimpleIntegerProperty populationProperty() {
        return population;
    }

    public void setPopulation(int population) {
        this.population.set(population);
    }
}

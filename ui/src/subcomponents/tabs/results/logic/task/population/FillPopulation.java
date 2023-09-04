package subcomponents.tabs.results.logic.task.population;

public class FillPopulation {
    private String entityName;
    private int population;


    public FillPopulation(String entityName, int population) {
        this.entityName = entityName;
        this.population = population;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}

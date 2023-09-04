package dto.entity;

import dto.property.DtoProperty;
import dto.range.DtoRange;

import java.util.ArrayList;
import java.util.List;

public class DtoEntity {
    private String entityName;
    private int population;
    private List<DtoProperty> propertyList;
    private List<Integer> populationHistoryList;

    public DtoEntity(String entityName, int population, List<Integer> populationHistoryList) {
        this.entityName = entityName;
        this.population = population;
        this.propertyList = new ArrayList<>();
        this.populationHistoryList = populationHistoryList;
    }

    public String getEntityName() {
        return entityName;
    }

    public int getPopulation() {
        return population;
    }

    public List<DtoProperty> getPropertyList() {
        return propertyList;
    }

    public void printEntityStructure(){
        final int[] count = {1}; // Declare an array to hold the count

        System.out.println("Entity name: "+ entityName);
        System.out.println("Entity population: " + population);
        System.out.println("Property list: ");
        System.out.println("---------------------------");

        propertyList.forEach(value -> {
            System.out.println("Property " + count[0] + " information:");
            value.printPropertyStructure();
            System.out.println();
            count[0]++; // Increment the count
        });
    }

    public void addPropertyToEntity(String propertyName, String propertyType, DtoRange range, boolean isInitRandom) {
        propertyList.add(new DtoProperty(propertyName, propertyType, range, isInitRandom));
    }

    public List<Integer> getPopulationHistoryList() {
        return populationHistoryList;
    }

    public void setPopulationHistoryList(List<Integer> populationHistoryList) {
        this.populationHistoryList = populationHistoryList;
    }
}

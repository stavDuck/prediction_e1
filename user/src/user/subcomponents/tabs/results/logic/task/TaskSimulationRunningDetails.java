package user.subcomponents.tabs.results.logic.task;

import dto.Dto;
import engine.simulation.Simulation;
import engine.simulation.execution.Status;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import user.subcomponents.tabs.results.logic.task.population.FillPopulation;

import java.util.Map;

public class TaskSimulationRunningDetails {
    private int simulationId;
    private Simulation simulation;
    private SimpleLongProperty propertyCurrTick;
    private VBox simulationDetails;
    //private Timeline timeline;
    private SimpleLongProperty runningSeconds;
    private TableView<FillPopulation> entityPopulation;
    private Map<String, SimpleIntegerProperty> propertyMap;
    private SimpleStringProperty errorStopSimulation;


    public TaskSimulationRunningDetails(int simulationId, Simulation simulation, SimpleLongProperty propertyCurrTick,
                                        SimpleLongProperty runningSeconds,VBox simulationDetails, TableView<FillPopulation> entityPopulation, Map<String, SimpleIntegerProperty> propertyMap,
                                        SimpleStringProperty errorStopSimulation ) {
        this.simulationId = simulationId;
        this.simulation = simulation;
        this.propertyCurrTick = propertyCurrTick;
        this.runningSeconds = runningSeconds;
        this.entityPopulation = entityPopulation;
        this.simulationDetails = simulationDetails;
        this.propertyMap = propertyMap;
        this.errorStopSimulation = errorStopSimulation;
    }

    public void runTask(){
        System.out.println("TaskSimulationDetails START");
        Status status;
        boolean isSelected;
        // Do while + change to funcition that return DTO in Simulation by ID
        //SimulationExecution currSimulation = simulation.getSimulationById(simulationId);
        // get status
        do{
            status = simulation.getSimulationById(simulationId).getSimulationStatus();
            Dto dto = simulation.getSimulationById(simulationId).createWorldDto();
            isSelected = simulation.getSimulationById(simulationId).isSimulationSelected();
            System.out.println(simulationId + " " + isSelected);
          //  System.out.println("TaskSimulationDetails> id: " + simulationId);
            //System.out.println("TaskSimulationDetails> " + simulation.getSimulationById(simulationId).isSimulationSelected());


            // function in Simulation.getDtoDetailByID
            Platform.runLater(() -> {
                runningSeconds.set(simulation.getSimulationById(simulationId).getRunningSeconds());
                propertyCurrTick.set(dto.getCurrTicks());

                for(FillPopulation row : entityPopulation.getItems()) {
                    int listSize = dto.getEntities().get(row.getEntityName()).getPopulationHistoryList().size();
                    if(listSize > 0) {
                        Integer newPopulaion = dto.getEntities().get(row.getEntityName()).getPopulationHistoryList().get(listSize - 1).getPopValue();
                        propertyMap.get(row.getEntityName()).setValue(newPopulaion);
                    }
                }
            });
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // Handle the InterruptedException if needed
            }
        }
        while (status == Status.IN_PROGRESS && isSelected);

        //timeline.stop();

        if(simulation.getSimulationById(simulationId).getSimulationStatus() == Status.FINISH){
            Platform.runLater(() -> {
                Label temp = ((Label) ((HBox)simulationDetails.getChildren().get(simulationId-1))
                        .getChildren().get(1));
                temp.setText(" Finish");
                temp.setStyle("-fx-text-fill:GREEN");
            });
        }

        else if(simulation.getSimulationById(simulationId).getSimulationStatus() == Status.STOP){
            Platform.runLater(() -> {
                errorStopSimulation.set(simulation.getSimulationById(simulationId).getErrorStopSimulation());
            });
        }


        System.out.println("TaskSimulationDetails END");
    }

}

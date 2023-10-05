package admin.subcomponents.tabs.results;

import dto.Dto;
import dto.entity.DtoEntity;
import dto.entity.Pair;
import dto.property.DtoProperty;
import engine.simulation.execution.SimulationExecution;
import engine.simulation.execution.Status;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import admin.subcomponents.app.AppController;
import admin.subcomponents.tabs.results.histogram.average.AverageValueComponentController;
import admin.subcomponents.tabs.results.histogram.consistency.ConsistencyComponentController;
import admin.subcomponents.tabs.results.histogram.entities.EntitiesHistogramComponentController;
import admin.subcomponents.tabs.results.logic.task.TaskSimulationPause;
import admin.subcomponents.tabs.results.logic.task.TaskSimulationResume;
import admin.subcomponents.tabs.results.logic.task.TaskSimulationRunningDetails;
import admin.subcomponents.tabs.results.logic.task.TaskSimulationStop;
import admin.subcomponents.tabs.results.logic.task.population.FillPopulation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ResultsComponentController {
    private final static String ROOT_TITEL = "Choose Entity";

    // tab controllers
    @FXML private ScrollPane entitiesHistogramComponent;
    @FXML private EntitiesHistogramComponentController entitiesHistogramComponentController;
    @FXML private ScrollPane consistencyComponent;
    @FXML private ConsistencyComponentController consistencyComponentController;

    @FXML private ScrollPane averageValueComponent;
    @FXML private AverageValueComponentController averageValueComponentController;


    @FXML
    private VBox simulationDetails;
    @FXML
    private VBox simulationProgressDetails;
    @FXML
    private HBox singleSimulationDetails;
    @FXML
    private Label simulationID;
    @FXML
    private ImageView progressImage;
    @FXML
    private Button pauseButton;
    @FXML
    private Button resumeButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button rerunButton;
    @FXML
    private GridPane entityGrid;
    @FXML
    private Label currTickLabel;
    @FXML
    private Label runningTimeLabel;
    @FXML
    private LineChart<?,?> popultionGraph;
    //@FXML
    //private CategoryAxis entityNameCategory;
    @FXML
    private NumberAxis entityNameCategory;

    @FXML
    private NumberAxis ticksNumberCategory;

    @FXML
    private TreeView<String> histoeamEntityTree;
    @FXML
    private Label entityNameStaticLabel;
    @FXML
    private Label populationStaticLabel;
    @FXML private Label selectedSimulationId;
    private SimpleLongProperty propertyCurrTick;
    private SimpleLongProperty runningTimeProperty;
    private SimpleLongProperty population;
    private AppController mainController;
    private Timeline runningTime;
    private Integer secondsCount = 1;
    private Map<String, SimpleIntegerProperty> propertyMap;
    private TableView<FillPopulation> populationTableView;
    private TaskSimulationRunningDetails task;
    @FXML private Label stopInformationLabel;
    private SimpleStringProperty propertyStopInformationLabel;
    public ResultsComponentController(){
        propertyCurrTick = new SimpleLongProperty();
        propertyStopInformationLabel = new SimpleStringProperty();
        propertyMap = new HashMap<>();
        runningTimeProperty = new SimpleLongProperty();
        //simulationProgressDetails = new VBox();
        //populationTableView = new TableView<>();
    }
    @FXML
    void initialize() {
        currTickLabel.textProperty().bind(Bindings.format("%,d", propertyCurrTick));
        runningTimeLabel.textProperty().bind(Bindings.format("%,d", runningTimeProperty));
        stopInformationLabel.textProperty().bind(Bindings.format("%s", propertyStopInformationLabel));
        //entityNameCategory.setTickUnit(1); // Set the tick unit to 1 to display only integers
        entityNameCategory.setLowerBound(0);
        initPopulationTable();

    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void addSimulation(int simulationID, boolean isFinished) {
        String text = "Simulation no. " + simulationID;
        simulationDetails.getChildren().add(createSimulationHbox(text));

    }

    public HBox createSimulationHbox(String text) {
        Image image;
        Label labelSimulationId = new Label(text);
        Label labelSimulationStatus = new Label(" Process");
        HBox dynamicVBox = new HBox();
        dynamicVBox.getChildren().addAll(labelSimulationId,labelSimulationStatus);

        ////////////// On click On Simulation //////////////////////
        EventHandler<MouseEvent> HBoxClickHandler = event -> {
            HBox clicked = (HBox) event.getSource();
            int index = simulationDetails.getChildren().indexOf(clicked);

            // if clicked on diffrent simulation show update details
            //if(index + 1 != mainController.getModel().getCurrSimulationId()) {
                // CLEAR INFORMATION
                clearAllHistogramTabs();
                clearStopInformationError();
                clearSimulationProgressDetails();
                clearTreeViewHistogram();

                // set curr simulation on the currect simulation
                mainController.getModel().getCurrSimulation().setSimulationSelected(false);
                mainController.getModel().setCurrSimulationId(index + 1);
                setSelectedSimulationId(index+1);
                mainController.getModel().getCurrSimulation().setSimulationSelected(true);
                addEntityToTable();

                // set view tree with entities
                loadHistoeamEntityTreeView();

                new Thread(() -> {
                    this.task = new TaskSimulationRunningDetails(mainController.getModel().getCurrSimulationId(), mainController.getModel().getSimulation(),
                            propertyCurrTick, runningTimeProperty, simulationDetails, populationTableView, propertyMap, propertyStopInformationLabel);
                    task.runTask();

                }).start();

                setPropertyLineChart();
                System.out.println("Label clicked: " + ((Label) clicked.getChildren().get(0)).getText());
         //   }
        };

        dynamicVBox.setOnMouseClicked(HBoxClickHandler);

        return dynamicVBox;
    }


    public void runSimulation() {
        mainController.getModel().runSimulation();
        mainController.getModel().getCurrSimulation().setSimulationSelected(true);
        new Thread(()->{
                this.task = new TaskSimulationRunningDetails(mainController.getModel().getCurrSimulationId(),mainController.getModel().getSimulation(),
                        propertyCurrTick, runningTimeProperty,simulationDetails , populationTableView, propertyMap, propertyStopInformationLabel);
            Platform.runLater(() -> {
                addEntityToTable(); // Update the UI component in the JavaFX Application Thread
            });
                task.runTask();
            }).start();
    }

    @FXML
    void pauseOnclick(ActionEvent event) {
        new Thread(new TaskSimulationPause(
                mainController.getModel().getCurrSimulationId(),mainController.getModel().getSimulation(), simulationDetails
        )).start();
    }

    @FXML
    void resumeOnclick(ActionEvent event) {
        new Thread(new TaskSimulationResume(
                mainController.getModel().getCurrSimulationId(),mainController.getModel().getSimulation(),propertyCurrTick,
                runningTimeProperty,simulationDetails, populationTableView, propertyMap, propertyStopInformationLabel
        )).start();
    }

    @FXML
    void stopOnClick(ActionEvent event) {
        new Thread(new TaskSimulationStop(
                mainController.getModel().getCurrSimulationId(),mainController.getModel().getSimulation(), simulationDetails, populationTableView, propertyMap
        )).start();
    }

    public void setPropertyLineChart(){
        SimulationExecution simulationExecution = mainController.getModel().getCurrSimulation(); // last function updated the index if the curr simulation
        if(simulationExecution.getSimulationStatus() == Status.FINISH || simulationExecution.getSimulationStatus() == Status.STOP) {
            // Clear the chart by removing all data series
            popultionGraph.getData().clear();
            Dto dto = mainController.getModel().getDtoWorld();
           // int maxNumOfPopulationValues = getMaxPopulationListSize(dto.getEntities());
          //  int maxNumOfTicks = dto.getCurrTicks();

           // entityNameCategory.setTickUnit(maxNumOfPopulationValues/20);
            //ticksNumberCategory.setTickUnit(maxNumOfTicks/20);

            for (String entityName : dto.getEntities().keySet()) {
                //XYChart.Series<Integer, Integer> series = new XYChart.Series<>();
                XYChart.Series series = new XYChart.Series();
                series.setName(entityName);


                for (Pair currPair : dto.getEntities().get(entityName).getPopulationHistoryList()) {
                    series.getData().add(new XYChart.Data(currPair.getTickNum(), currPair.getPopValue()));
                }
                popultionGraph.getData().add(series);
            }
        }
    }

    public void addEntityToTable() {
        //TableView<FillPopulation> tableView;
        /*if(this.populationTableView.getItems().isEmpty()) {
            //tableView = new TableView<>();
            //entityTable = new TableView<FillPopulation>();
            TableColumn entityName = new TableColumn<FillPopulation, String>("Entity Name");
            entityName.setCellValueFactory(new PropertyValueFactory<>("entityName"));
            TableColumn population = new TableColumn<FillPopulation, Integer>("Current Population");
            population.setCellValueFactory(new PropertyValueFactory<>("population"));
            this.populationTableView.getColumns().add(entityName);
            this.populationTableView.getColumns().add(population);
            this.populationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            simulationProgressDetails.getChildren().add(populationTableView);

        }
        else {
            this.populationTableView.getItems().clear();
        }*/
        this.populationTableView.getItems().clear();
        for(DtoEntity currEntityName : mainController.getDtoWorld().getEntities().values()) {
            FillPopulation row = new FillPopulation(currEntityName.getEntityName());
            propertyMap.put(currEntityName.getEntityName(), new SimpleIntegerProperty());
            row.populationProperty().bind(propertyMap.get(currEntityName.getEntityName()));
            this.populationTableView.getItems().add(row);


        }
    }

    private int getMaxPopulationListSize(Map<String, DtoEntity> entities) {
        int max = -1; // population always above or equal 0

        for(DtoEntity currEntity : entities.values()){
            if(currEntity.getPopulationHistoryList().size() > max){
                max = currEntity.getPopulationHistoryList().size();
            }
        }
        return max;
    }

    public void loadHistoeamEntityTreeView(){
        Dto dto = mainController.getDtoWorld();
        Collection<DtoEntity> dtoEntityLst = dto.getEntities().values();

        // clear if tree has iformation
       if(histoeamEntityTree.getRoot() != null && histoeamEntityTree.getRoot().getChildren() != null){
           histoeamEntityTree.getRoot().getChildren().clear();
           clearAllHistogramTabs();
       }

        // set the tree root
        TreeItem<String> rootItem = new TreeItem<>(ROOT_TITEL);

        // create view of entities by entities names
        if(dtoEntityLst!= null && !dtoEntityLst.isEmpty()) {
            // add leafs by entities names
            for (DtoEntity curr : dtoEntityLst){
                TreeItem<String> entityItem = new TreeItem<>(curr.getEntityName());

                // add all properties of the Entity under it
                for(DtoProperty currProp : curr.getPropertyList()){
                    entityItem.getChildren().add(new TreeItem<>(currProp.getName()));
                }

                // add entity to branch entities
                rootItem.getChildren().add(entityItem);
            }
        }
        // set information in tree
        histoeamEntityTree.setRoot(rootItem);
    }

    // for selected tree view
    @FXML
    void selectedEntityProperty(MouseEvent event){
        Dto dto = mainController.getDtoWorld();
        SimulationExecution currSimulation = mainController.getModel().getCurrSimulation();
        TreeItem<String> item = histoeamEntityTree.getSelectionModel().getSelectedItem();

        if(item != null && !item.getValue().equals(ROOT_TITEL)) {
            // Clear the tabs information !!!!!
            clearAllHistogramTabs();

            // expected to get the title -> ENTITIES_TITLE if click is relevant
            TreeItem<String> ifoType = item.getParent();

            // check if click is valid -> only clicking on property
            if(!ifoType.getValue().equals(ROOT_TITEL) &&
                    ifoType.getParent().getValue().equals(ROOT_TITEL)){
                String entityName = item.getParent().getValue();
                String propertyName = item.getValue();

                // show information only when simulation is finished
                if(currSimulation.getSimulationStatus() == Status.STOP ||
                        currSimulation.getSimulationStatus() == Status.FINISH){
                    entitiesHistogramComponentController.presentHistogramByEntityAndValue(currSimulation, entityName, propertyName);
                    consistencyComponentController.presentConsistency(currSimulation, entityName, propertyName);
                    averageValueComponentController.presentAveragePropertyValue(currSimulation, entityName, propertyName);
                }

                System.out.println("Entity: " + entityName + " Property: " + propertyName);
            }
        }
    }

    public void clearAllHistogramTabs(){
        averageValueComponentController.clearTxt();
        consistencyComponentController.clearTxt();
        entitiesHistogramComponentController.clearTxt();
    }

    public void clearPopulationList() {
        this.populationTableView.getItems().clear();
    }
    public void clearStopInformationError(){
        propertyStopInformationLabel.set("");
    }

    public void clearSelectSimulationList(){
        simulationDetails.getChildren().clear();
    }
    public void clearSimulationProgressDetails(){
       propertyCurrTick.setValue(0);
       runningTimeProperty.setValue(0);
    }
    public void clearTreeViewHistogram() {
        if(histoeamEntityTree.getRoot()!= null &&
                histoeamEntityTree.getRoot().getChildren() != null) {
            histoeamEntityTree.getRoot().getChildren().clear();
        }
    }

    public void clearPopulationChart(){
        if(popultionGraph.getData() != null) {
            popultionGraph.getData().clear();
        }
    }
    public void clearPopulationTable(){
        this.populationTableView.getItems().clear();
    }
    public void setSelectedSimulationId(int index){
            if(index != -1) {
                selectedSimulationId.setText("Selected Simulation: " + index);
            }
            else{
                selectedSimulationId.setText("Selected Simulation: ");
            }
    }

    @FXML
    public void rerunOnClick(ActionEvent actionEvent) {
        mainController.moveToExecutionTab();
    }

    public void initPopulationTable() {
        this.populationTableView = new TableView<>();
        TableColumn entityName = new TableColumn<FillPopulation, String>("Entity Name");
        entityName.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        TableColumn population = new TableColumn<FillPopulation, Integer>("Current Population");
        population.setCellValueFactory(new PropertyValueFactory<>("population"));
        this.populationTableView.getColumns().add(entityName);
        this.populationTableView.getColumns().add(population);
        this.populationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        simulationProgressDetails.getChildren().add(populationTableView);
    }
}

package subcomponents.tabs.results;

import dto.Dto;
import dto.entity.DtoEntity;
import engine.entity.EntityStructure;
import engine.simulation.Simulation;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.omg.CosNaming.BindingIterator;
import subcomponents.app.AppController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultsComponentController {
    @FXML
    private VBox simulationDetails;
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
    private Label entityNameLabel;
    @FXML
    private Label populationLabel;
    @FXML
    private LineChart<?,?> popultionGraph;
    //@FXML
    //private CategoryAxis entityNameCategory;
    @FXML
    private NumberAxis entityNameCategory;

    @FXML
    private NumberAxis ticksNumberCategory;

    @FXML
    private TreeView<?> histoeamEntityTree;
    @FXML
    private Tab populationHistogramTab;
    @FXML
    private Tab consistencyTab;
    @FXML
    private Tab averageValueTab;
    @FXML
    private Label entityNameStaticLabel;
    @FXML
    private Label populationStaticLabel;
    private SimpleLongProperty currTick;
    private SimpleLongProperty runningTime;
    private SimpleLongProperty population;
    private AppController mainController;

    @FXML
    void initialize() {
        currTickLabel.textProperty().bind(Bindings.format("%,d", currTick));
        runningTimeLabel.textProperty().bind(Bindings.format("%,d", runningTime));
        entityNameCategory.setTickUnit(1); // Set the tick unit to 1 to display only integers
        entityNameCategory.setLowerBound(0);
        populationLabel.textProperty().bind(Bindings.format("%,d", population));
    }
   /* @FXML
    void viewSimulationDetails(MouseEvent event) {
        Label label = (Label) event.getSource();
        Pattern pattern = Pattern.compile("\\d+");

        // Create a matcher for the input string
        Matcher matcher = pattern.matcher(label.getText());
        // Find the first match
        if (matcher.find()) {
            String index = matcher.group(); // Get the matched number as a string
            int parsedNumber = Integer.parseInt(index); // Convert it to an integer if needed
            Simulation simulation = mainController.getModel().getSimulationById(parsedNumber);
            entityPopulation(simulation);
        }
    }*/

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void addSimulation(int simulationID, boolean isFinished) {
        String text = simulationID + ". " + simulationID;
        simulationDetails.getChildren().add(createSimulationHbox(text, isFinished));
    }

    public HBox createSimulationHbox(String text, boolean isFinished) {
        Image image;
        Label label = new Label(text);
       /* label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Handle the click event here
                viewSimulationDetails(event);
            }
        });
        if(isFinished) {
            image = new Image("file:C:\\Users\\USER\\IdeaProjects\\prediction_e1\\ui\\src\\subcomponents\\tabs\\results\\similationFinished.png");
        }
        else {
            image = new Image("file:C:\\Users\\USER\\IdeaProjects\\prediction_e1\\ui\\src\\subcomponents\\tabs\\results\\inProgress.png");
        }
        ImageView progressImage = new ImageView(image);
        progressImage.setFitWidth(30);
        progressImage.setFitHeight(30);*/
        HBox dynamicVBox = new HBox();
        //dynamicVBox.getChildren().addAll(label, progressImage);
        dynamicVBox.getChildren().addAll(label);
        //dynamicVBox.setPadding(new Insets(5, 5, 5, 5));
        return dynamicVBox;

    }

    public void entityPopulation(Simulation simulation) {
        int rowIndex = 0;
        for(EntityStructure entity : simulation.getWorld().getEntityStructures().values()) {
            Label name = new Label(entity.getEntityName());
            entityGrid.add(entityNameStaticLabel, 0, rowIndex);
            entityGrid.add(name, 1, rowIndex);
            entityGrid.add(populationStaticLabel, 2, rowIndex);
            Label pop = new Label();
            pop.textProperty().bind(Bindings.format("%,d", population));
            rowIndex++;
        }

    }


    public void runSimulation(String fileName) {
        mainController.getModel().runSimulation(fileName);
    }


    public void setPropertyLineChart(){

        if(mainController.getModel().getSimulationDone()) {
            // Clear the chart by removing all data series
            popultionGraph.getData().clear();
            Dto dto = mainController.getModel().getDtoWorld();

            for (String entityName : dto.getEntities().keySet()) {
                //XYChart.Series<Integer, Integer> series = new XYChart.Series<>();
                XYChart.Series series = new XYChart.Series();
                series.setName(entityName);
                int index = 0;

                for (Integer amount : dto.getEntities().get(entityName).getPopulationHistoryList()) {
                    series.getData().add(new XYChart.Data(index + 1, amount));
                    index++;
                }
                popultionGraph.getData().add(series);
            }
        }
    }

    @FXML
    void clickOnSimulation(MouseEvent event) {
        setPropertyLineChart();

    }

}

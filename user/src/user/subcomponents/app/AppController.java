package user.subcomponents.app;

import dto.Dto;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import user.login.LoginController;
import okhttp3.*;
import user.subcomponents.app.task.TaskThreadPoolUpdater;
import user.subcomponents.common.ResourcesConstants;
import user.subcomponents.model.Model;
import user.subcomponents.tabs.details.DetailsComponentController;
import user.subcomponents.tabs.execution.ExecutionComponentController;
import user.subcomponents.tabs.results.ResultsComponentController;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static user.util.http.HttpClientUtil.HTTP_CLIENT;

public class AppController {
    private Model model;
    // peaces to replace
    @FXML private ScrollPane mainScroller;
    @FXML private BorderPane mainApplicationScreen;

    // login controller to replace on click
    private GridPane loginComponent;
    private LoginController logicController;

    @FXML private ScrollPane detailsTab;
    @FXML private DetailsComponentController detailsTabController;
    @FXML private ScrollPane executionTab;
    @FXML private ExecutionComponentController executionTabController;
    @FXML private ScrollPane resultTab;
    @FXML private ResultsComponentController resultTabController;

    @FXML
    private TabPane tabPane;
    @FXML
    private Label titlePrediction;
    @FXML
    private Button openFileButton;
    @FXML
    private Label selectedFileName;
    @FXML
    private Label messageToUser;
    @FXML
    private Label waitingThreadPoolLabel;
    private SimpleLongProperty propertyWaitingThreadPool;
    @FXML
    private Label runningThreadPoolLabel;
    private SimpleLongProperty propertyRunningThreadPool;
    @FXML
    private Label completedThreadPoolLabel;
    private SimpleLongProperty propertyCompletedThreadPool;

    @FXML
    private GridPane gridPanelThreadsPool;
    @FXML
    private Tab detailsTabComponent;
    @FXML
    private Tab executionTabComponent;
    @FXML
    private Tab resultTabComponent;
    private Stage primaryStage;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;

    private StopTaskObject stopThreadPool;

    public AppController() {
        stopThreadPool = new StopTaskObject();
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        propertyWaitingThreadPool = new SimpleLongProperty();
        propertyRunningThreadPool = new SimpleLongProperty();
        propertyCompletedThreadPool = new SimpleLongProperty();


    }

    @FXML
    public void initialize() {
     loadLoginPage();

    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(ResourcesConstants.LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            logicController = fxmlLoader.getController();
            logicController.setAppController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setMainPanelTo(Parent pane) {
        mainScroller.setContent(pane);
    }
    public void switchToMainApplication() {
        setMainPanelTo(mainApplicationScreen);
        initializeMainApplication();
    }

    public void initializeMainApplication(){
        if (detailsTabController != null && executionTabController != null && resultTabController != null) {
            detailsTabController.setMainController(this);
            executionTabController.setMainController(this);
            resultTabController.setMainController(this);
        }

        selectedFileName.textProperty().bind(selectedFileProperty);
        waitingThreadPoolLabel.textProperty().bind(Bindings.format("%,d", propertyWaitingThreadPool));
        runningThreadPoolLabel.textProperty().bind(Bindings.format("%,d", propertyRunningThreadPool));
        completedThreadPoolLabel.textProperty().bind(Bindings.format("%,d", propertyCompletedThreadPool));
        propertyWaitingThreadPool.set(0);
        propertyRunningThreadPool.set(0);
        propertyCompletedThreadPool.set(0);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @FXML
    void openFileButtonAction(ActionEvent event) throws IOException {
        // reset value in threadpool - if boolean is true thread pool has already ran
        if(stopThreadPool.isStop()){
            stopThreadPool.setStop(false);
            propertyWaitingThreadPool.set(0);
            propertyRunningThreadPool.set(0);
            propertyCompletedThreadPool.set(0);
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml file", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        selectedFileProperty.set(absolutePath);
        isFileSelected.set(true);

        // NEW !!!
        loadXmlRequest(absolutePath);



       /* String res = model.loadXmlFile(absolutePath);
        //messageToUser.setText(res.isEmpty() ? "Successful" : res);

        // clear !!
        resultTabController.clearAllHistogramTabs();
        resultTabController.clearStopInformationError();
        resultTabController.clearSimulationProgressDetails();
        resultTabController.clearTreeViewHistogram();
        resultTabController.clearSelectSimulationList();
        resultTabController.clearPopulationChart();
        resultTabController.setSelectedSimulationId(-1);
        resultTabController.clearPopulationList();
        // NEED TO CLEAR THE TABLE IN SCREEN 3



        showPopup(res);
        detailsTabController.loadDetailsView();
        executionTabController.populateTab();*/
    }

    private void loadXmlRequest(String absolutePath) throws IOException {
        File file = new File(absolutePath);
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("file1", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
                .build();
        String finalUrl = ResourcesConstants.UPLOAD_XML;

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(requestBody)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        okhttp3.Response response = call.execute();
        System.out.println(response.body().string());
        // HttpClientUtil.postRunASync(requestBody, finalUrl, Callback callback)
    }

    public void runTaskThreadPool(){
        // create task to update the thread pool information
        stopThreadPool.setStop(true);
        new Thread(new TaskThreadPoolUpdater(model.getSimulation(),
                propertyWaitingThreadPool, propertyRunningThreadPool, propertyCompletedThreadPool, stopThreadPool)).start();
    }

    public void showPopup(String message) {
        Popup popup = new Popup();

        Label popupLabel = new Label(message);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> popup.hide());

        VBox popupContent = new VBox(10); // Use VBox layout
        popupContent.setStyle("-fx-background-color: white" + ";-fx-border-color: #007bff" + "; -fx-padding: 10;");
        popupContent.getChildren().addAll(popupLabel);

        HBox buttonContainer = new HBox(closeButton); // Place the button in an HBox
        //need to place the popup
        buttonContainer.setAlignment(Pos.TOP_CENTER);
        popupContent.getChildren().add(buttonContainer); // Add the button container to the VBox
        popup.getContent().add(popupContent);

        popup.show(primaryStage);
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Dto getDtoWorld(){
        return model.getDtoWorld();
    }

    public Model getModel() {
        return model;
    }

    public void setExecutionComponentController(ExecutionComponentController executionTabController) {
        this.executionTabController = executionTabController;
        executionTabController.setMainController(this);
    }

    public void startExecutionTab() {
        executionTabController.populateTab();
    }

    public TabPane getTabPane() {
        return tabPane;
    }
    public void moveToResultsTab() {
        tabPane.getSelectionModel().select(resultTabComponent);
       // resultTabController.addSimulation(model.getSimulation().getSimulationID(), model.getSimulationDone());
        resultTabController.addSimulation(model.getCurrSimulationId(), false);
        resultTabController.setSelectedSimulationId(model.getCurrSimulationId());

        // Clear !!
        resultTabController.clearAllHistogramTabs();
        resultTabController.clearStopInformationError();
        resultTabController.clearSimulationProgressDetails();
        resultTabController.clearTreeViewHistogram();
        resultTabController.clearPopulationChart();
        resultTabController.clearPopulationTable();

        // set view tree with entities
        resultTabController.loadHistoeamEntityTreeView();
        resultTabController.clearStopInformationError();
    }

    public void runSimulation() {
        resultTabController.runSimulation();
    }

    public String getCurrLoadedFileName(){
        return selectedFileProperty.get();
    }

    public void moveToExecutionTab() {
        tabPane.getSelectionModel().select(executionTabComponent);
        executionTabController.populateTabFromRerunSimulation();
    }
}

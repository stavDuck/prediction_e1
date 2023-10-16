package user.subcomponents.app;

import com.sun.istack.internal.NotNull;
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
import user.subcomponents.app.task.TaskDetailsUpdater;
import user.subcomponents.app.task.TaskThreadPoolUpdater;
import user.subcomponents.common.ResourcesConstants;
import user.subcomponents.model.Model;
import user.subcomponents.tabs.details.DetailsComponentController;
import user.subcomponents.tabs.execution.ExecutionComponentController;
import user.subcomponents.tabs.requests.RequestComponentController;
import user.subcomponents.tabs.results.ResultsComponentController;
import user.util.http.HttpUserUtil;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class AppController implements Closeable {
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
    @FXML private ScrollPane requestTab;
    @FXML private RequestComponentController requestTabController;

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
    @FXML
    private Tab requestTabComponent;
    @FXML private Label userNameLabel;
    private Stage primaryStage;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;

    private StopTaskObject stopThreadPool;

    public AppController() {
        stopThreadPool = new StopTaskObject();
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        /*propertyWaitingThreadPool = new SimpleLongProperty();
        propertyRunningThreadPool = new SimpleLongProperty();
        propertyCompletedThreadPool = new SimpleLongProperty();*/
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
        if (detailsTabController != null && executionTabController != null
                && resultTabController != null && requestTabController != null) {
            detailsTabController.setMainController(this);
            executionTabController.setMainController(this);
            resultTabController.setMainController(this);
            requestTabController.setMainController(this);
        }
        // set connection to details updater
        runTaskDetailsUpdater();
        //detailsTabController.runTaskDetailsUpdater();

       /* selectedFileName.textProperty().bind(selectedFileProperty);
        waitingThreadPoolLabel.textProperty().bind(Bindings.format("%,d", propertyWaitingThreadPool));
        runningThreadPoolLabel.textProperty().bind(Bindings.format("%,d", propertyRunningThreadPool));
        completedThreadPoolLabel.textProperty().bind(Bindings.format("%,d", propertyCompletedThreadPool));
        propertyWaitingThreadPool.set(0);
        propertyRunningThreadPool.set(0);
        propertyCompletedThreadPool.set(0);*/
    }

    public void setModel(Model model) {
        this.model = model;
    }
    public void runTaskDetailsUpdater(){
        // create task to update the thread pool information
        detailsTabController.setStopDetailsUpdater(true);
        new Thread(new TaskDetailsUpdater(detailsTabController.getStopDetailsUpdaterThread(),model, detailsTabController, requestTabController)).start();
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

    public   Stage getPrimaryStage() {return this.primaryStage;}
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
       // resultTabController.addSimulation(model.getCurrSimulationId(), false);
        //resultTabController.setSelectedSimulationId(model.getCurrSimulationId());

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

    // close application will remove the username from the current user manager list
    @Override
    public void close() throws IOException {
        //usersListComponentController.close();
        logout();
    }

    public void logout(){
        // stop taskUpdater in Details controller
        detailsTabController.setStopDetailsUpdater(false);
        HttpUserUtil.runAsync(ResourcesConstants.LOGOUT, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Logout request ended with failure...");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful() || response.isRedirect()) {
                    HttpUserUtil.removeCookiesOf(ResourcesConstants.BASE_DOMAIN);
                    System.out.println(response.body().string());
                }
            }
        });
    }


    /*public void runSimulation() {
        resultTabController.runSimulation();
    }*/

    public String getCurrLoadedFileName(){
        return selectedFileProperty.get();
    }

    public void moveToExecutionTab() {
        tabPane.getSelectionModel().select(executionTabComponent);
      //  executionTabController.populateTabFromRerunSimulation();
    }

    public void updateUserName(String userName) {
        userNameLabel.setText(userName);
    }

    public String getUserNameValue(){
        return userNameLabel.getText();
    }
}

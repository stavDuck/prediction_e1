package admin.subcomponents.app;

import admin.subcomponents.common.ResourcesConstants;
import admin.subcomponents.model.Model;
import admin.subcomponents.tabs.allocations.AllocationComponentController;
import admin.subcomponents.tabs.details.DetailsComponentController;
import admin.subcomponents.tabs.results.ResultsComponentController;
import admin.util.http.HttpAdminUtil;
import dto.Dto;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import admin.subcomponents.app.task.TaskThreadPoolUpdater;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class AppController implements Closeable {
    private Model model;

    @FXML private ScrollPane detailsTab;
    @FXML private DetailsComponentController detailsTabController;
    @FXML private ScrollPane allocationTab;
    @FXML private AllocationComponentController allocationTabController;
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

    /*@FXML
    private Label waitingThreadPoolLabel;
    private SimpleLongProperty propertyWaitingThreadPool;*/
    /*@FXML
    private Label runningThreadPoolLabel;
    private SimpleLongProperty propertyRunningThreadPool;*/
   /* @FXML
    private Label completedThreadPoolLabel;
    private SimpleLongProperty propertyCompletedThreadPool;*/

   /* @FXML
    private GridPane gridPanelThreadsPool;*/
    @FXML
    private Tab detailsTabComponent;
    @FXML
    private Tab allocationTabComponent;
    @FXML
    private Tab resultTabComponent;
    private Stage primaryStage;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;

    //private StopTaskObject stopThreadPool;

    public AppController() {
        //stopThreadPool = new StopTaskObject();
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
       // propertyWaitingThreadPool = new SimpleLongProperty();
       // propertyRunningThreadPool = new SimpleLongProperty();
       // propertyCompletedThreadPool = new SimpleLongProperty();


    }

    @FXML
    public void initialize() {
        if (detailsTabController != null && allocationTabController != null && resultTabController != null) {
            detailsTabController.setMainController(this);
            allocationTabController.setMainController(this);
            resultTabController.setMainController(this);
        }

        selectedFileName.textProperty().bind(selectedFileProperty);
        //waitingThreadPoolLabel.textProperty().bind(Bindings.format("%,d", propertyWaitingThreadPool));
       // runningThreadPoolLabel.textProperty().bind(Bindings.format("%,d", propertyRunningThreadPool));
       // completedThreadPoolLabel.textProperty().bind(Bindings.format("%,d", propertyCompletedThreadPool));
       // propertyWaitingThreadPool.set(0);
       // propertyRunningThreadPool.set(0);
       // propertyCompletedThreadPool.set(0);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @FXML
    void openFileButtonAction(ActionEvent event) throws IOException {
        // reset value in threadpool - if boolean is true thread pool has already ran
        if(detailsTabController.getStopThreadPool().isStop()){
            detailsTabController.setStopThreadPool(false);
           detailsTabController.resetThreadPoolLabelsInformation();
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

        // Create a custom Callback
        Callback customCallback = new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Handle successful response here
                    String responseBody = response.body().string();
                    System.out.println("Response: " + responseBody);

                    // clear !!
                    resultTabController.clearAllHistogramTabs();
                    resultTabController.clearStopInformationError();
                    resultTabController.clearSimulationProgressDetails();
                    resultTabController.clearTreeViewHistogram();
                    resultTabController.clearSelectSimulationList();
                    resultTabController.clearPopulationChart();
                    resultTabController.setSelectedSimulationId(-1);
                    resultTabController.clearPopulationList();

                    showPopup("File loaded successfully");
                    detailsTabController.loadDetailsView();
                    //executionTabController.populateTab();

                } else {
                    // Handle unsuccessful response here
                    System.out.println("Request was not successful. Response code: " + response.code());
                    showPopup(response.body().string() + "\n please try to fix the issue and reload the xml again");
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        showPopup("Something went wrong: " + e.getMessage())
                );
            }
        };

       // Call call = HTTP_CLIENT.newCall(request);
       // okhttp3.Response response = call.execute();

        HttpAdminUtil.postRunASync(requestBody, finalUrl, customCallback);
    }

    /*public void runTaskThreadPool(){
        // create task to update the thread pool information
        stopThreadPool.setStop(true);
        new Thread(new TaskThreadPoolUpdater(model.getSimulation(),
                propertyWaitingThreadPool, propertyRunningThreadPool, propertyCompletedThreadPool, stopThreadPool)).start();
    }*/

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

    public void setAllocationComponentController(AllocationComponentController allocationTabController) {
        this.allocationTabController = allocationTabController;
        allocationTabController.setMainController(this);
    }

    /*public void startExecutionTab() {
        executionTabController.populateTab();
    }*/

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


    @Override
    public void close() throws IOException {
        //usersListComponentController.close();
        logout();
    }

    public void logout(){
        HttpAdminUtil.runAsync(ResourcesConstants.LOGOUT_ADMIN, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Logout request ended with failure...");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful() || response.isRedirect()) {
                    HttpAdminUtil.removeCookiesOf(ResourcesConstants.BASE_DOMAIN);
                    System.out.println(response.body().string());
                }
            }
        });
    }


    public void runSimulation() {
        resultTabController.runSimulation();
    }

    public String getCurrLoadedFileName(){
        return selectedFileProperty.get();
    }

    public void moveToExecutionTab() {
        //tabPane.getSelectionModel().select(executionTabComponent);
        //executionTabController.populateTabFromRerunSimulation();
    }
}

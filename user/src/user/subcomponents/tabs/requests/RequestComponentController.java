package user.subcomponents.tabs.requests;

import com.google.gson.Gson;
import dto.termination.DtoTermination;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import request.DtoRequest;
import user.subcomponents.app.AppController;
import user.subcomponents.app.task.TaskRequestTableUpdater;
import user.subcomponents.common.ResourcesConstants;
import user.util.http.HttpUserUtil;

import java.io.IOException;
import java.util.*;

public class RequestComponentController {
    private AppController mainController;
    @FXML
    private ChoiceBox<String> simulationNameChoiceBox;
    private List<String> simulationNamesArr;
    @FXML
    private TextField executionNumberTextField;
    @FXML
    private CheckBox byTimeCheckBox;
    @FXML
    private TextField byTimeTextField;
    @FXML
    private CheckBox byTicksCheckBox;
    @FXML
    private TextField byTicksTextField;
    @FXML
    private CheckBox byUserCheckBox;
    @FXML
    private Button submitNewRequestButton;
    @FXML
    private TableView<DtoRequest> requsetUserTable;
    private Map<Integer,DtoRequest> userRequestsMap; // key = request id, value = request dto
    private boolean isFirstTimeSubmitRequest;
    // all Table Columns
    @FXML private TableColumn<DtoRequest, String> reqIdTableColumn;
    @FXML private TableColumn<DtoRequest, String> simulationNameTableColumn;
    @FXML private TableColumn<DtoRequest, String> userNameTableColumn;
    @FXML private TableColumn<DtoRequest, String> requestedRunsTableColumn;
    @FXML private TableColumn<DtoRequest, String> endConditionTableColumn;
    @FXML private TableColumn<DtoRequest, String> requestStatusTableColumn;
    @FXML private TableColumn<DtoRequest, String> runningTableColumn;
    @FXML private TableColumn<DtoRequest, String> finishedTableColumn;
    @FXML private TableColumn<DtoRequest, Button> executeTableColumn;

    @FXML
    public void initialize() {
        isFirstTimeSubmitRequest = true; // set start value
        simulationNamesArr = new ArrayList<>();
        userRequestsMap = new HashMap<>();
        // setup all the colums according to the DtoRequest meta data
        reqIdTableColumn.setCellValueFactory(cellData -> {
            String requestIdStr = cellData.getValue().getRequestId().toString();
            return new SimpleStringProperty(requestIdStr);
        });
        simulationNameTableColumn.setCellValueFactory(cellData -> {
            String simulationName = cellData.getValue().getSimulationXmlName();
            return new SimpleStringProperty(simulationName);
        });
        userNameTableColumn.setCellValueFactory(cellData -> {
            String userName = cellData.getValue().getUserName();
            return new SimpleStringProperty(userName);
        });
        requestedRunsTableColumn.setCellValueFactory(cellData -> {
            String requestedRuns = cellData.getValue().getSimulationRequestedRuns().toString();
            return new SimpleStringProperty(requestedRuns);
        });
        endConditionTableColumn.setCellValueFactory(cellData -> {
            String endCondition = getEndConditionString(cellData.getValue().getTerminationConditions());
            return new SimpleStringProperty(endCondition);
        });
        requestStatusTableColumn.setCellValueFactory(cellData -> {
            String status = cellData.getValue().getStatus();
            return new SimpleStringProperty(status);
        });
        runningTableColumn.setCellValueFactory(cellData -> {
            String running = cellData.getValue().getSimulationCurrentRunning().toString();
            return new SimpleStringProperty(running);
        });
        finishedTableColumn.setCellValueFactory(cellData -> {
            Integer finishedInt = cellData.getValue().getSimulationRequestedRuns() - cellData.getValue().getSimulationLeftoverRuns();
            String finished = finishedInt.toString();
            return new SimpleStringProperty(finished);
        });


        // create a button to execute only if the status is accepted
        executeTableColumn.setCellValueFactory(param ->
                {
                    if(param.getValue().getStatus().equals("approved") && param.getValue().getSimulationLeftoverRuns() >0) {
                        SimpleObjectProperty<Button> buttonProperty = new SimpleObjectProperty<>(new Button("Execute"));
                        {
                            buttonProperty.get().setOnAction(event -> {
                                DtoRequest dtoRequest = param.getValue();
                                // Handel on click - execute simulation !
                                handleExecuteButtonClick(dtoRequest);
                            });
                        }

                        ObservableValue<Button> SimpleObjectProperty = buttonProperty;
                        return SimpleObjectProperty;
                    }
                    else{
                        return null;
                    }
            });
    }

    public void handleExecuteButtonClick(DtoRequest dtoRequest) {
        System.out.println("Request ID: " + dtoRequest.getRequestId());
        // need to support chancing the number of left runs and everything
        mainController.startExecutionTab(dtoRequest.getSimulationXmlName());
    }

    // Function helper to set end condition colum in request table
    private String getEndConditionString(DtoTermination terminationConditions) {
        String res = "";
        if(terminationConditions.getBySeconds() == null && terminationConditions.getByTick() == null){
            res = "By User";
        }
        else{ // not by user
            if(terminationConditions.getBySeconds() != null){
                res += "By Seconds : " + terminationConditions.getBySeconds();
            }

            if(terminationConditions.getByTick() != null && res.equals("")){
                res += "By Ticks : " + terminationConditions.getByTick();
            } else if (terminationConditions.getByTick() != null && !res.equals("")) {
                res += ", By Ticks : " + terminationConditions.getByTick();
            }
        }

        return res;
    }

    public void setSimulationNamesArr(List<String> updatedList) {
        simulationNamesArr = updatedList;
    }

    public void updateChoiceBoxWithNewArr(List<String> updatedList) {
        int idexSelected;
        if ((updatedList.size() != 0) &&
                (simulationNamesArr.size() != updatedList.size())) {
            setSimulationNamesArr(updatedList);
            // save the last selected item
            idexSelected = simulationNameChoiceBox.getSelectionModel().getSelectedIndex();
            ObservableList<String> newList = FXCollections.observableList(updatedList);
            simulationNameChoiceBox.setItems(newList);
            // make sure the last selected is still selected even when the list is updated
            simulationNameChoiceBox.getSelectionModel().select(idexSelected);
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public Map<Integer, DtoRequest> getUserRequestsMap() {
        return userRequestsMap;
    }

    @FXML
    void submitNewRequestButtonAction(ActionEvent event) {
        try {
            DtoRequest dtoRequest = validateRequestInformation();
            // if response successful return the dto request and set in the local map
            sendNewRequestFromUser(dtoRequest);

            } catch (RuntimeException e) {
            mainController.showPopup("Error: " + e.getMessage() + ",\n please fix and try again.");
        }
    }

    // validate the information, throw exception if something is invalid, return the dto request to send to server
    public DtoRequest validateRequestInformation() throws RuntimeException {
        DtoRequest dtoRequest;
        // request ID will be null - creating new request
        String xmlName;
        Integer simulationRequestedRuns;
        // Termination Logic
        boolean byTimeCheckBoxBool;
        Integer byTimeValue;
        boolean byTicksCheckBoxBool;
        Integer byTickValue;
        boolean byUserCheckBoxBool;

        // set selected XML name
        if (simulationNameChoiceBox.getValue().equals("") || simulationNameChoiceBox.getValue() == null) {
            throw new RuntimeException("Please select a valid XML name in \"Simulation Name\"");
        } else {
            xmlName = simulationNameChoiceBox.getValue();
        }

        // set executions number for xml
        try {
            simulationRequestedRuns = Integer.parseInt(executionNumberTextField.getText());
        } catch (NumberFormatException e) {
            throw new RuntimeException("\"Execution Number\" is not a number");
        }

        // Set by time condition
        if (byTimeCheckBox.isSelected()) {
            byTimeCheckBoxBool = true;
            try {
                byTimeValue = Integer.parseInt(byTimeTextField.getText());
            } catch (NumberFormatException e) {
                throw new RuntimeException("Condition \"By Time\" is not a number");
            }
        } else {
            byTimeCheckBoxBool = false;
            byTimeValue = null;
        }

        // Set by ticks
        if (byTicksCheckBox.isSelected()) {
            byTicksCheckBoxBool = true;
            try {
                byTickValue = Integer.parseInt(byTicksTextField.getText());
            } catch (NumberFormatException e) {
                throw new RuntimeException("Condition \"By Ticks\" is not a number");
            }
        } else {
            byTicksCheckBoxBool = false;
            byTickValue = null;
        }

        // Set by user
        byUserCheckBoxBool = byUserCheckBox.isSelected();

        // validate Termination condition is VALID
        // The conditions can be By Time and/or By Ticks, or By User Only
        if ((byUserCheckBoxBool && (byTicksCheckBoxBool || byTimeCheckBoxBool)) ||
                (!byUserCheckBoxBool && !byTicksCheckBoxBool && !byTimeCheckBoxBool)) {
            throw new RuntimeException("Termination conditions can be By Time and/or By Ticks, or By User Only");
        }

        // create DtoRequest
        DtoTermination dtoTermination = new DtoTermination(byTickValue, byTimeValue);
        return new DtoRequest(null, xmlName, simulationRequestedRuns, null, null,
                dtoTermination, "pending", mainController.getUserNameValue());
    }
    // send a new request information to save in the server, if response is successful return the copy of dto information
    public void sendNewRequestFromUser(DtoRequest dtoRequest) {
        // create a http request and add new request
        //Gson gson = new Gson();
        Gson gson = ResourcesConstants.GSON_INSTANCE;
        String requestBodyString = gson.toJson(dtoRequest);
        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
        RequestBody requestBody = RequestBody.create(requestBodyString, mediaType);

        String finalUrl = HttpUrl
                .parse(ResourcesConstants.ADD_NEW_REQUEST_FROM_USER)
                .newBuilder()
                .build()
                .toString();

        System.out.println("New request is launched for: " + finalUrl);

        HttpUserUtil.postRunASync(requestBody, finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("AddNewRequestFromUser is failed with error: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            System.out.println("Something went wrong: " + responseBody)
                    );
                } else {
                    DtoRequest dtoRequestFromResponseBody = gson.fromJson(responseBody, DtoRequest.class);
                    userRequestsMap.put(dtoRequestFromResponseBody.getRequestId(), dtoRequestFromResponseBody);
                    System.out.println("Added new Request by user");

                    // update the request table
                    updateRequestTableInfo();

                    // create new task to get updates on requests statuses if it's the first request
                    // If it's first time clicking on submit new request -
                    // start a task check updates for spesific user's requests
                    if(isFirstTimeSubmitRequest){
                        isFirstTimeSubmitRequest = false;
                       // create new task to get updates
                        runTaskRequestTableUpdater();
                    }
                }
            }
        });
    }

    public void updateRequestTableInfo(){
        List<DtoRequest> list = new ArrayList<>(userRequestsMap.values());
        ObservableList<DtoRequest> newObserveList = FXCollections.observableList(list);

        requsetUserTable.setItems(newObserveList);
        requsetUserTable.refresh();
    }

    public void runTaskRequestTableUpdater(){
        // create task to update the request table information
        new Thread(new TaskRequestTableUpdater(this, mainController.getUserNameValue())).start();
    }
}

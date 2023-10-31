package admin.subcomponents.tabs.allocations;

import admin.subcomponents.app.AppController;
import admin.subcomponents.app.task.TaskRequestTableAdminUpdater;
import admin.subcomponents.common.ResourcesConstants;
import admin.util.http.HttpAdminUtil;
import dto.termination.DtoTermination;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import request.DtoRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllocationComponentController {
    private AppController mainController;
    private Map<Integer,DtoRequest> adminRequestsMap; // key = request id, value = request dto

    // All table properties
    @FXML
    private TableView<DtoRequest> requsetAdminTable;
    // all table columns
    @FXML private TableColumn<DtoRequest, String> reqIdAdminTableColumn;
    @FXML private TableColumn<DtoRequest, String> simulationNameAdminTableColumn;
    @FXML private TableColumn<DtoRequest, String> userNameAdminTableColumn;
    @FXML private TableColumn<DtoRequest, String> requestedRunsAdminTableColumn;
    @FXML private TableColumn<DtoRequest, String> endConditionAdminTableColumn;
    @FXML private TableColumn<DtoRequest, String> requestStatusAdminTableColumn;
    @FXML private TableColumn<DtoRequest, String> runningAdminTableColumn;
    @FXML private TableColumn<DtoRequest, String> finishedAdminTableColumn;
    @FXML private TableColumn<DtoRequest, Button> approveAdminTableColumn;
    @FXML private TableColumn<DtoRequest, Button> denyAdminTableColumn;

    @FXML
    public void initialize() {
        adminRequestsMap = new HashMap<>();

        // setup all the colums according to the DtoRequest meta data
        reqIdAdminTableColumn.setCellValueFactory(cellData -> {
            String requestIdStr = cellData.getValue().getRequestId().toString();
            return new SimpleStringProperty(requestIdStr);
        });
        simulationNameAdminTableColumn.setCellValueFactory(cellData -> {
            String simulationName = cellData.getValue().getSimulationXmlName();
            return new SimpleStringProperty(simulationName);
        });
        userNameAdminTableColumn.setCellValueFactory(cellData -> {
            String userName = cellData.getValue().getUserName();
            return new SimpleStringProperty(userName);
        });
        requestedRunsAdminTableColumn.setCellValueFactory(cellData -> {
            String requestedRuns = cellData.getValue().getSimulationRequestedRuns().toString();
            return new SimpleStringProperty(requestedRuns);
        });
        endConditionAdminTableColumn.setCellValueFactory(cellData -> {
            String endCondition = getEndConditionString(cellData.getValue().getTerminationConditions());
            return new SimpleStringProperty(endCondition);
        });
        requestStatusAdminTableColumn.setCellValueFactory(cellData -> {
            String status = cellData.getValue().getStatus();
            return new SimpleStringProperty(status);
        });
        runningAdminTableColumn.setCellValueFactory(cellData -> {
            String running = cellData.getValue().getSimulationCurrentRunning().toString();
            return new SimpleStringProperty(running);
        });
        finishedAdminTableColumn.setCellValueFactory(cellData -> {
            Integer finishedInt = cellData.getValue().getSimulationFinishedRuns();
            String finished = finishedInt.toString();
            return new SimpleStringProperty(finished);
        });

        // create a button to Approve user's request only if the status is pending
        approveAdminTableColumn.setCellValueFactory(param ->
        {
            if(param.getValue().getStatus().equals(ResourcesConstants.PENDING_STATUS)) {
                SimpleObjectProperty<Button> buttonProperty = new SimpleObjectProperty<>(new Button("Approve"));
                {
                    buttonProperty.get().setOnAction(event -> {
                        DtoRequest dtoRequest = param.getValue();
                        // Handel on click - execute simulation !
                        handleApproveButtonClick(dtoRequest);
                    });
                }
                ObservableValue<Button> SimpleObjectProperty = buttonProperty;
                return SimpleObjectProperty;
            }
            else{
                return null;
            }
        });

        // create a button to Deny user's request only if the status is pending
        denyAdminTableColumn.setCellValueFactory(param ->
        {
            if(param.getValue().getStatus().equals(ResourcesConstants.PENDING_STATUS)) {
                SimpleObjectProperty<Button> buttonProperty = new SimpleObjectProperty<>(new Button("Deny"));
                {
                    buttonProperty.get().setOnAction(event -> {
                        DtoRequest dtoRequest = param.getValue();
                        // Handel on click - execute simulation !
                        handleDenyButtonClick(dtoRequest);
                    });
                }
                ObservableValue<Button> SimpleObjectProperty = buttonProperty;
                return SimpleObjectProperty;
            }
            else{
                return null;
            }
        });
        // run table view task
        runTaskRequestTableUpdater();
    }

    // when clicking on deny button admin is updating the request status from pending to "deny"
    private void handleDenyButtonClick(DtoRequest dtoRequest) {
        updateRequestStatus(dtoRequest, ResourcesConstants.DENIED_STATUS);
    }

    // when clicking on approve button admin is updating the request status from pending to "approve"
    public void handleApproveButtonClick(DtoRequest dtoRequest) {
        updateRequestStatus(dtoRequest, ResourcesConstants.APPROVED_STATUS);
    }

    public void updateRequestStatus(DtoRequest dtoRequest, String status){
        // creating http request to update the status
        String finalUrl = HttpUrl
                .parse(ResourcesConstants.UPDATE_REQUEST_STATUS)
                .newBuilder()
                .addQueryParameter("username", dtoRequest.getUserName())
                .addQueryParameter("requestid", dtoRequest.getRequestId().toString())
                .addQueryParameter("newstatus", status)
                .build()
                .toString();

        System.out.println("New request is launched for: " + finalUrl);

        HttpAdminUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("updateRequestStatus went wrong: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    System.out.println("response of updateRequestStatus went wrong: " + responseBody);
                } else {
                    System.out.println("response of updateRequestStatus is successful");
                    Platform.runLater(() -> {
                       // need to update the local map with the updated status
                        adminRequestsMap.get(dtoRequest.getRequestId()).setStatus(status);
                        updateRequestTableInfo();
                    });
                }
            }
        });
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


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void updateRequestTableInfo(){
        List<DtoRequest> list = new ArrayList<>(adminRequestsMap.values());
        ObservableList<DtoRequest> newObserveList = FXCollections.observableList(list);

        requsetAdminTable.setItems(newObserveList);
        requsetAdminTable.refresh();
    }

    public Map<Integer, DtoRequest> getAdminRequestsMap() {
        return adminRequestsMap;
    }

    public void setAdminRequestsMap(Map<Integer, DtoRequest> adminRequestsMap) {
        this.adminRequestsMap = adminRequestsMap;
    }

    public void runTaskRequestTableUpdater(){
        // create task to update the request table information
        new Thread(new TaskRequestTableAdminUpdater(this)).start();
    }


}

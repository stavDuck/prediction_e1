package admin.subcomponents.app.task;

import admin.subcomponents.app.StopTaskObject;
import admin.subcomponents.common.ResourcesConstants;
import admin.subcomponents.tabs.allocations.AllocationComponentController;
import admin.util.http.HttpAdminUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import request.DtoRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class TaskRequestTableAdminUpdater implements Runnable{
    private AllocationComponentController allocationComponentController;
    private StopTaskObject stopThread;

    public TaskRequestTableAdminUpdater(AllocationComponentController allocationComponentController) {
        this.allocationComponentController = allocationComponentController;
        stopThread = new StopTaskObject();
        stopThread.setStop(true);
    }

    // every 2 sec it will go and take update of all information from server
   // go over every information in response, if not exist - add to the map
    // if exist check what need to be updated
    // boolean if need to change - if false no change if true send "updateRequestTableInfo"
    @Override
    public void run() {
        Gson gson = ResourcesConstants.GSON_INSTANCE;
        System.out.println("TaskRequestTableAdminUpdater START");
        do {
            try {
                // get the list of request under the user
                Response response = createRequestToGetALLRequestsList();

                if (response.isSuccessful()) {
                    // convert the iformation from body into list of dtoRequests
                    Type dtoRequestMapType = new TypeToken<Map<Integer, DtoRequest>>() {}.getType();
                    String responseBody = null;
                    try {
                        responseBody = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }

                    Map<Integer, DtoRequest> tempDtoRequestMap = gson.fromJson(responseBody, dtoRequestMapType);
                    // update the list in requestController according to current information
                    Boolean isNeedToUpdate = checkChangesInRequestList(tempDtoRequestMap);

                    if (isNeedToUpdate) {
                        Platform.runLater(() -> {
                            System.out.println("UPDATING ADMIN TABLE VIEW");
                            allocationComponentController.updateRequestTableInfo();
                        });
                    }
                }
                response.close();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // Handle the InterruptedException if needed
                }
            }
            catch (RuntimeException e){
                System.out.println(e.getMessage());
            }
        }
        while(stopThread.isStop());

        System.out.println("TaskRequestTableAdminUpdater END");
    }


    private Response createRequestToGetALLRequestsList() {
        String finalUrl = HttpUrl
                .parse(ResourcesConstants.GET_ALL_USERS_REQUESTS)
                .newBuilder()
                .build()
                .toString();

        //System.out.println("New request is launched for: " + finalUrl);

        Request request = new Request.Builder()
                .url(finalUrl).build();
        Call call = HttpAdminUtil.HTTP_CLIENT.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return response;
    }

    // function go over all the list of request from the response, if found request need to add or update-
    // update the requestMap in the controller
    // return if any updates in the requestMap has been done
    private Boolean checkChangesInRequestList(Map<Integer, DtoRequest> tempDtoRequestMap) {
       boolean isNeedToUpdate = false;
        for(DtoRequest curreDtoRequest : tempDtoRequestMap.values()){
            DtoRequest tableViewDtoRequest = allocationComponentController.getAdminRequestsMap().get(curreDtoRequest.getRequestId());
            // if Dto is not exist - this is new request and need to add
            if(tableViewDtoRequest == null){
                allocationComponentController.getAdminRequestsMap().put(curreDtoRequest.getRequestId(), curreDtoRequest);
                isNeedToUpdate = true;
            }
            // else - need to check if any information is changed
            else{
                if((curreDtoRequest.getSimulationCurrentRunning() != tableViewDtoRequest.getSimulationCurrentRunning()) ||
                        curreDtoRequest.getSimulationLeftoverRuns() != tableViewDtoRequest.getSimulationLeftoverRuns()  ||
                    curreDtoRequest.getSimulationFinishedRuns() != tableViewDtoRequest.getSimulationFinishedRuns()){
                    // override the request
                    allocationComponentController.getAdminRequestsMap().put(curreDtoRequest.getRequestId(), curreDtoRequest);
                    isNeedToUpdate = true;
                }
            }
        }

        return isNeedToUpdate;
    }
}

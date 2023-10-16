package user.subcomponents.app.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.operations.Bool;
import dto.Dto;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import request.DtoRequest;
import user.subcomponents.app.StopTaskObject;
import user.subcomponents.common.ResourcesConstants;
import user.subcomponents.tabs.requests.RequestComponentController;
import user.util.http.HttpUserUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TaskRequestTableUpdater implements Runnable{
    private RequestComponentController requestComponentController;
    private StopTaskObject stopThread;
    private String userName;

    public TaskRequestTableUpdater(RequestComponentController requestComponentController, String userName) {
        this.requestComponentController = requestComponentController;
        this.userName = userName;
        stopThread = new StopTaskObject();
        stopThread.setStop(true);
    }

    @Override
    public void run() {
        //Gson gson = new Gson();
        Gson gson = ResourcesConstants.GSON_INSTANCE;
        System.out.println("TaskRequestTableUpdater START");

        do {
            try {
                // get the list of request under the user
                Response response = createRequestToGetRequestsListForUser();

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
                            System.out.println("UPDATING TABLE VIEW");
                            requestComponentController.updateRequestTableInfo();
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

        System.out.println("TaskRequestTableUpdater END");
    }

    // Function create Http request to get all the user's request from the server
    private Response createRequestToGetRequestsListForUser() {
        String finalUrl = HttpUrl
                .parse(ResourcesConstants.GET_USER_REQUESTS)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        System.out.println("New request is launched for: " + finalUrl);

        Request request = new Request.Builder()
                .url(finalUrl).build();
        Call call = HttpUserUtil.HTTP_CLIENT.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return response;
    }

    private Boolean checkChangesInRequestList(Map<Integer, DtoRequest> requestMap) {
        Boolean isNeedToUpdate = false;
        DtoRequest dtoRequestFromController;

        // the number of request will be the same
        for(DtoRequest currDtoRequest : requestMap.values()){
            // get DtoRequest reference from controller and update the status
            dtoRequestFromController = requestComponentController.getUserRequestsMap().get(currDtoRequest.getRequestId());
            if(dtoRequestFromController != null && (!currDtoRequest.getStatus().equals(dtoRequestFromController))){
                isNeedToUpdate = true;
                dtoRequestFromController.setStatus(currDtoRequest.getStatus());
            }
        }

        return isNeedToUpdate;
    }
}

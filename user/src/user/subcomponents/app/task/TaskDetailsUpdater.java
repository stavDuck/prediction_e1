package user.subcomponents.app.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.Dto;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import user.subcomponents.app.AppController;
import user.subcomponents.app.StopTaskObject;
import user.subcomponents.common.ResourcesConstants;
import user.subcomponents.model.Model;
import user.subcomponents.tabs.details.DetailsComponentController;
import user.util.http.HttpUserUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class TaskDetailsUpdater implements Runnable{
    private StopTaskObject stopThread;
    private Model model;
    private DetailsComponentController detailsComponentController;

    public TaskDetailsUpdater(StopTaskObject stopThread, Model model, DetailsComponentController detailsComponentController) {
        this.stopThread = stopThread;
        this.model = model;
        this.detailsComponentController = detailsComponentController;
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        System.out.println("TaskDetailsUpdater START");
        do {
            // get list of all curr dtos
            String finalUrl = HttpUrl
                    .parse(ResourcesConstants.GET_DTO_XML_MAP)
                    .newBuilder()
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
                throw new RuntimeException(e);
            }

            if(response.isSuccessful()){
                System.out.println("Response from getXmlsServlet is valid");
                Type dtoXmlMapType = new TypeToken<Map<String, Dto>>() { }.getType();
                String responseBody = null;
                try {
                    responseBody = response.body().string();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Map<String, Dto> tempDtoMap = gson.fromJson(responseBody, dtoXmlMapType);

                // upate model xml list
                Map<String, Dto> currDtoMap = model.getDtoXmlManager().getDtoXmlManager();
                // if the size diffrent - need to update the dto list,
                // dto cnnot be delete so the size only can get bigger
                if( (tempDtoMap.values().size() != 0) && (currDtoMap.values().size() != tempDtoMap.values().size())){
                    model.getDtoXmlManager().setDtoXmlManager(tempDtoMap);
                    // set update details controller
                    Platform.runLater(() -> {
                        detailsComponentController.loadDetailsView();
                    });
                }
            }else {
                System.out.println("Response from getXmlsServlet is NOT valid");
            }
            response.close();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // Handle the InterruptedException if needed
            }
        }
        while(stopThread.isStop());

        System.out.println("TaskDetailsUpdater END");
    }
}

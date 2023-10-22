package admin.subcomponents.app.task;

import admin.subcomponents.app.StopTaskObject;
import admin.subcomponents.common.ResourcesConstants;
import admin.util.http.HttpAdminUtil;
import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.Const;
import dto.threadpool.DtoThreadPool;
import engine.simulation.Simulation;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TaskThreadPoolUpdater implements Runnable{
    private final SimpleLongProperty propertyWaitingThreadPool;
    private final SimpleLongProperty propertyRunningThreadPool;
    private final SimpleLongProperty propertyCompletedThreadPool;
    private StopTaskObject stopThread;

    public TaskThreadPoolUpdater(SimpleLongProperty propertyWaitingThreadPool,SimpleLongProperty propertyRunningThreadPool,
                                 SimpleLongProperty propertyCompletedThreadPool, StopTaskObject stopThread ) {
        this.propertyWaitingThreadPool = propertyWaitingThreadPool;
        this.propertyRunningThreadPool = propertyRunningThreadPool;
        this.propertyCompletedThreadPool = propertyCompletedThreadPool;
        this.stopThread = stopThread;
    }

    @Override
    public void run() {
        //Gson gson = new Gson();
        Gson gson = ResourcesConstants.GSON_INSTANCE;
        System.out.println("TaskThreadPoolUpdater START");

        do{
            Response response = getThreadPoolInformation();
            if((response != null) && response.isSuccessful()){
                DtoThreadPool dtoThreadPool = null;
                try {
                    dtoThreadPool = gson.fromJson(response.body().string(), DtoThreadPool.class);

                    // send HTTP request and get DtoThreadPool information
                    int waitingThreads = dtoThreadPool.getWaitingThreads();
                    int runningThreads = dtoThreadPool.getRunningThreads();
                    int completedThreads =dtoThreadPool.getCompletedThreads();

                    Platform.runLater(() -> {
                        propertyWaitingThreadPool.set(waitingThreads);
                        propertyRunningThreadPool.set(runningThreads);
                        propertyCompletedThreadPool.set(completedThreads);
                    });

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            else{
                System.out.println("Response GetThreadPoolInfoServlet NOT valid");
            }
          //  System.out.println(stopThread.isStop());

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Handle the InterruptedException if needed
            }
        }
        while(stopThread.isStop());

        System.out.println("TaskThreadPoolUpdater END");
    }

    private Response getThreadPoolInformation() {
        String finalUrl = HttpUrl
                .parse(ResourcesConstants.GET_THREAD_POOL_INFO)
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
            throw new RuntimeException(e);
        }
        return response;
    }
}

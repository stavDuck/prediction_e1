package admin.main;
import admin.subcomponents.app.AppController;
import admin.subcomponents.common.ResourcesConstants;
import admin.subcomponents.model.Model;
import admin.util.http.HttpAdminUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;


public class Main extends Application {
    private Model model;
    private AppController currAppController;
    private boolean isAdminValid = false;

    @Override
    public void start(Stage primaryStage) throws Exception{
        model = new Model();
        FXMLLoader fxmlLoader = new FXMLLoader();
        //setAdminLoggedIn(customCallback);
        setAdminLoggedIn();

        if(isAdminValid) {
            // load main fxml
            //URL url = getClass().getResource(ResourcesConstants.MAIN_FXML_RESOURCE_IDENTIFIER);
            URL url = getClass().getResource(ResourcesConstants.APP_FXML_INCLUDE_RESOURCE);
            fxmlLoader.setLocation(url);
            Parent root = fxmlLoader.load(url.openStream());

            // wire up controller
            AppController controller = fxmlLoader.getController();
            currAppController = controller;
            controller.setModel(model);
            controller.setPrimaryStage(primaryStage);
            //controller.setExecutionComponentController(executionComponentController);

            // set stage
            primaryStage.setTitle("Prediction");
            Scene scene = new Scene(root, 1050, 600);

            //scene.getStylesheets().add(getClass().getResource(ResourcesConstants.MAIN_FXML_RESOURCE_CSS).toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        else{
            // load fxml with error only one admin can be logged in at once
            //URL url = getClass().getResource(ResourcesConstants.MAIN_FXML_RESOURCE_IDENTIFIER);
            URL url = getClass().getResource(ResourcesConstants.ERROR_LOGIN_FXML_INCLUDE_RESOURCE);
            fxmlLoader.setLocation(url);
            Parent root = fxmlLoader.load(url.openStream());
            // set stage
            primaryStage.setTitle("Prediction");
            Scene scene = new Scene(root, 1050, 600);

            //scene.getStylesheets().add(getClass().getResource(ResourcesConstants.MAIN_FXML_RESOURCE_CSS).toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

   // public void setAdminLoggedIn(Callback callback) {
   public void setAdminLoggedIn() throws IOException {
        String finalUrl = HttpUrl
                .parse(ResourcesConstants.LOGIN_ADMIN)
                .newBuilder()
                .build()
                .toString();

        System.out.println("New request is launched for: " + finalUrl);
        //HttpAdminUtil.runAsync(finalUrl, callback);
       Request request = new Request.Builder()
               .url(finalUrl).build();
       Call call = HttpAdminUtil.HTTP_CLIENT.newCall(request);
       Response response = call.execute();

       if(response.isSuccessful()){
           isAdminValid = true;
           System.out.println("Response is valid");
       }else {
           System.out.println("Response is NOT valid");
       }
       response.close();
    }

    @Override
    public void stop() throws Exception {
        if(isAdminValid) {
            currAppController.close();
            HttpAdminUtil.shutdown();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}
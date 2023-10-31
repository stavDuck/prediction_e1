package main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import login.LoginController;

import java.io.IOException;
import java.net.URL;

import static constants.Constants.LOGIN_PAGE_FXML_RESOURCE_LOCATION;

public class ChatClient extends Application {

    //private ChatAppMainController chatAppMainController;
    private LoginController controller;
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Chat App Client");

        //URL loginPage = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        URL loginPage = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            controller = fxmlLoader.getController();

            Scene scene = new Scene(root, 700, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() throws Exception {
        //HttpClientUtil.shutdown();
        //chatAppMainController.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package main;
import common.ResourcesConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import screens.DetailsControler;

import java.net.URL;

public class Main extends Application {
   /* public static void main(String[] args) {
        Menu menu = new Menu();
        menu.startMenu();
    }*/


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();

        // load main fxml
        URL url = getClass().getResource(ResourcesConstants.MAIN_FXML_RESOURCE_IDENTIFIER);
        loader.setLocation(url);
        Parent root = loader.load(url.openStream());

        // wire up controller
        DetailsControler controller = loader.getController();
        controller.setPrimaryStage(primaryStage);


        // set stage
        primaryStage.setTitle("Prediction");
        Scene scene = new Scene(root, 1050, 600);

        scene.getStylesheets().add(getClass().getResource(ResourcesConstants.MAIN_FXML_RESOURCE_CSS).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
       // Application.launch(Main.class);
        launch(args);
    }


}
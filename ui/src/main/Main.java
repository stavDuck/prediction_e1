package main;
import menu.Menu;
import subcomponents.app.AppController;
import subcomponents.common.ResourcesConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import subcomponents.model.Model;
import subcomponents.tabs.execution.ExecutionComponentController;

import java.net.URL;


public class Main extends Application {
    // backup for testing - console version
//public class Main {
   /* public static void main(String[] args) {
        Menu menu = new Menu();
        menu.startMenu();
    }*/

    private Model model;

    @Override
    public void start(Stage primaryStage) throws Exception{
        model = new Model();
        FXMLLoader fxmlLoader = new FXMLLoader();

        // load main fxml
        //URL url = getClass().getResource(ResourcesConstants.MAIN_FXML_RESOURCE_IDENTIFIER);
        URL url = getClass().getResource(ResourcesConstants.APP_FXML_INCLUDE_RESOURCE);
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        // wire up controller
        AppController controller = fxmlLoader.getController();
        //ExecutionComponentController executionComponentController = fxmlLoader.getController();
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


    public static void main(String[] args) {
       // Application.launch(Main.class);
        launch(args);
    }


}
package user.main;
import user.subcomponents.app.AppController;
import user.subcomponents.common.ResourcesConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import user.subcomponents.model.Model;
import user.util.http.HttpUserUtil;

import java.net.URL;


public class Main extends Application {
    private Model model;
    private AppController currAppController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        model = new Model();
        FXMLLoader fxmlLoader = new FXMLLoader();

        // load main fxml
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
    @Override
    public void stop() throws Exception {
        currAppController.close();
        HttpUserUtil.shutdown();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
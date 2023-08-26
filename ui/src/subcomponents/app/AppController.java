package subcomponents.app;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import subcomponents.model.Model;
import subcomponents.tabs.details.DetailsComponentController;
import subcomponents.tabs.execution.ExecutionComponentController;
import subcomponents.tabs.results.ResultsComponentController;

import java.io.File;

public class AppController {
    private Model model;
    @FXML private ScrollPane detailsTab;
    @FXML private DetailsComponentController detailsTabController;
    @FXML private ScrollPane executionTab;
    @FXML private ExecutionComponentController executionTabController;
    @FXML private ScrollPane resultTab;
    @FXML private ResultsComponentController resultTabController;

    @FXML
    private Label titlePrediction;
    @FXML
    private Button openFileButton;
    @FXML
    private Label selectedFileName;
    @FXML
    private Label messageToUser;
    @FXML
    private GridPane gridPanelThreadsPool;
    @FXML
    private Tab detailsTabComponent;
    @FXML
    private Tab executionTabComponent;
    @FXML
    private Tab resultTabComponent;

    private Stage primaryStage;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;

    @FXML
    public void initialize() {
        if (detailsTabController != null && executionTabController != null && resultTabController != null) {
            detailsTabController.setMainController(this);
            executionTabController.setMainController(this);
            resultTabController.setMainController(this);
        }

        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        selectedFileName.textProperty().bind(selectedFileProperty);
    }


    public void setModel(Model model) {
        this.model = model;
    }

    @FXML
    void openFileButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml file", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        selectedFileProperty.set(absolutePath);
        isFileSelected.set(true);

        String res = model.loadXmlFile(absolutePath);
        messageToUser.setText(res.isEmpty() ? "Successful" : res);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


}

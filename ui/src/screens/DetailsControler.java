package screens;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class DetailsControler {

    @FXML
    private Label titlePrediction;
    @FXML
    private Button openFileButton;
    @FXML
    private Label selectedFileName;
    @FXML
    private GridPane gridPanelThreadsPool;
    @FXML
    private Button detailsButton;
    @FXML
    private Button newExecutaionButton;
    @FXML
    private Button resultsButton;
    @FXML
    private GridPane gridPanelDetailsView;
    @FXML
    private Label informationDetailsTitle;
    @FXML
    private TreeView<?> treeViewInformation;
    @FXML
    private TextArea informationDetailsBody;

    private Stage primaryStage;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;


    public DetailsControler(){
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);

    }

    @FXML
    private void initialize() {
        selectedFileName.textProperty().bind(selectedFileProperty);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    // Functions
    @FXML
    void detailsButtonAction(ActionEvent event) {

    }

    @FXML
    void newExecutaionButtonAction(ActionEvent event) {

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
    }

    @FXML
    void resultsButtonAction(ActionEvent event) {

    }

}

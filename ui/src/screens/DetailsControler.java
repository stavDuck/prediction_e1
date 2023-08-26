package screens;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sun.reflect.generics.tree.Tree;

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
    private TreeView<String> treeViewInformation;
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


        // set the tree root
        TreeItem<String> rootItem = new TreeItem<>("Information");
        // example for adding
        TreeItem<String> branchItem1 = new TreeItem<>("Entities");
        TreeItem<String> branchItem2 = new TreeItem<>("rules");
        TreeItem<String> leafItem1 = new TreeItem<>("Entities");

        branchItem1.getChildren().addAll(leafItem1);
        rootItem.getChildren().addAll(branchItem1,branchItem2);
        treeViewInformation.setRoot(rootItem);

    }

    @FXML
    void selectItem(MouseEvent event) {
        TreeItem<String> item = treeViewInformation.getSelectionModel().getSelectedItem();
        if(item != null){
            TreeItem<String> branchItem = new TreeItem<>("NEW");
            treeViewInformation.getRoot().getChildren().add(branchItem);
           // item.getParent().getValue(); // ???? to get if it is env, rule, entity
            // need to copy the information to the txt Erea
        }
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

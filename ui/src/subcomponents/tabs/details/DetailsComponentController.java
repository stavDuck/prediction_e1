package subcomponents.tabs.details;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import subcomponents.app.AppController;

public class DetailsComponentController {

    private AppController mainController;
    @FXML
    private GridPane gridPanelDetailsView;

    @FXML
    private Label informationDetailsTitle;

    @FXML
    private TreeView<String> treeViewInformation;

    @FXML
    private TextArea informationDetailsBody;

    @FXML
    void selectItem(MouseEvent event) {

    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

}

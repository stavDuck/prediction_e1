package user.subcomponents.tabs.requests;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import user.subcomponents.app.AppController;

public class RequestComponentController {
    @FXML
    private ComboBox<?> simulationNameComboBox;
    @FXML
    private TextField executionNumberTextField;
    @FXML
    private CheckBox byTimeCheckBox;
    @FXML
    private TextField byTimeTextField;
    @FXML
    private CheckBox byTicksCheckBox;
    @FXML
    private TextField byTicksTextField;
    @FXML
    private CheckBox byUserCheckBox;
    @FXML
    private Button submitNewRequestButton;
    @FXML
    private TableView<?> requsetUserTable;

    @FXML
    void submitNewRequestButtonAction(ActionEvent event) {

    }

    private AppController mainController;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

}

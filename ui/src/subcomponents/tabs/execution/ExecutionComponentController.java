package subcomponents.tabs.execution;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import subcomponents.app.AppController;

public class ExecutionComponentController {
    private AppController mainController;
    @FXML
    private VBox entityOne;

    @FXML
    private TextField entityOnePopulation;

    @FXML
    private VBox entityTwo;

    @FXML
    private TextField entityTwoPopulation;

    @FXML
    private CheckBox envVariableCheckbox;

    @FXML
    private TextField entityTwoPopulation1;

    @FXML
    private Button clearButton;

    @FXML
    private Button startButton;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

}

package subcomponents.tabs.execution;
import dto.entity.DtoEntity;
import dto.env.DtoEnv;
import engine.entity.EntityStructure;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import subcomponents.app.AppController;

public class ExecutionComponentController {
    private AppController mainController;
    @FXML
    private VBox entitiesVbox;
    @FXML
    private VBox entityOne;

    @FXML
    private TextField entityOnePopulation;

    @FXML
    private VBox entityTwo;

    @FXML
    private TextField entityTwoPopulation;

    @FXML
    private VBox envVariablesVbox;

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

    public void populateTab() {
        populateEntityNames();
    }

    public void populateEntityNames() {
        for(DtoEntity entity : mainController.getDtoWorld().getEntities().values()) {
            entitiesVbox.getChildren().add(createEntityVbox(entity.getEntityName(), "Population"));
        }
        for(DtoEnv env : mainController.getDtoWorld().getEnvs().values()) {
            envVariablesVbox.getChildren().add(createEnvVariableVbox(env.getEnvName(), env.getEnvType(), "Value"));
        }
    }

    public VBox createEntityVbox(String labelText, String textFieldPrompt) {
        Label label = new Label(labelText);
        TextField textField = new TextField();
        textField.setPromptText(textFieldPrompt);

        VBox dynamicVBox = new VBox(label, textField);
        dynamicVBox.setPadding(new Insets(5, 5, 5, 5));
        return dynamicVBox;
    }

    public VBox createEnvVariableVbox(String checkBoxLabelText, String typeLabel, String textFieldPrompt) {
        VBox dynamicBox;
        CheckBox checkBox = new CheckBox(checkBoxLabelText);
        checkBox.setPadding(new Insets(5, 0, 0, 0));
        Label label = new Label("Type: " + typeLabel.toLowerCase());
        if(typeLabel.equalsIgnoreCase("boolean")) {
            RadioButton trueButton = new RadioButton("True");
            RadioButton falseButton = new RadioButton("false");
            ToggleGroup toggleGroup = new ToggleGroup();
            trueButton.setToggleGroup(toggleGroup);
            falseButton.setToggleGroup(toggleGroup);
            dynamicBox = new VBox(checkBox, label, trueButton, falseButton);
            dynamicBox.setPadding(new Insets(5, 5, 5, 5));
        }
        else {
            TextField textField = new TextField();
            textField.setPromptText(textFieldPrompt);
            dynamicBox = new VBox(checkBox, label, textField);
            dynamicBox.setPadding(new Insets(5, 5, 5, 5));
        }
        return dynamicBox;
    }
}

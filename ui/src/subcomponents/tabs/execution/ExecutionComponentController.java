package subcomponents.tabs.execution;
import com.sun.corba.se.spi.ior.ObjectKey;
import dto.entity.DtoEntity;
import dto.env.DtoEnv;
import dto.range.DtoRange;
import engine.entity.EntityStructure;
import engine.property.PropertyInstance;
import engine.simulation.execution.Status;
import engine.value.generator.ValueGeneratorFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import subcomponents.app.AppController;

import java.util.regex.Pattern;

public class ExecutionComponentController {
    private static final String POPULATION_PROMPT_TEXT = "Population";
    private static final String VALUE_PROMPT_TEXT = "Value";
    private static final String TRUE_RADIO_BUTTON = "True";
    private static final String FALSE_RADIO_BUTTON = "False";
    private static final int ENTITY_VBOX_ENTITY_NAME_INDEX = 0;
    private static final int ENTITY_VBOX_POPULATION_TEXT_FIELD_INDEX = 1;
    private static final int ENTITY_VBOX_ERROR_MESSAGE_INDEX = 2;
    private static final int ENVIRONMENT_VBOX_CHECKBOX_INDEX = 0;
    private static final int ENVIRONMENT_VBOX_TYPE_INDEX = 1;
    private static final int ENVIRONMENT_VBOX_RADIO_BUTTON_TRUE_INDEX = 2;
    private static final int ENVIRONMENT_VBOX_RADIO_BUTTON_FALSE_INDEX = 3;
    private static final int ENVIRONMENT_VBOX_BOOLEAN_ERROR_MESSAGE_INDEX = 4;
    private static final int ENVIRONMENT_VBOX_VALUE_TEXT_FIELD_INDEX = 2;
    private static final int ENVIRONMENT_VBOX_TEXT_ERROR_MESSAGE_INDEX = 3;



    private AppController mainController;
    @FXML
    private VBox entitiesVbox;
    @FXML
    private VBox entityOne;
    @FXML
    private Label entityNameLabel;
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
        entitiesVbox.getChildren().clear();
        envVariablesVbox.getChildren().clear();
        populateEntityNames();
        populateEnvVariables();
    }

    public void populateEntityNames() {
        for(DtoEntity entity : mainController.getDtoWorld().getEntities().values()) {
            entitiesVbox.getChildren().add(createEntityVbox(entity.getEntityName(), POPULATION_PROMPT_TEXT));
        }
    }

    public void populateEnvVariables() {
        for(DtoEnv env : mainController.getDtoWorld().getEnvs().values()) {
            envVariablesVbox.getChildren().add(createEnvVariableVbox(env.getEnvName(), env.getEnvType(), VALUE_PROMPT_TEXT, env.getEnvRange()));
        }

    }

    public VBox createEntityVbox(String labelText, String textFieldPrompt) {
        Label label = new Label(labelText);
        TextField textField = new TextField();
        textField.setPromptText(textFieldPrompt);
        Label errorLabel = new Label();
        VBox dynamicVBox = new VBox(label, textField, errorLabel);
        dynamicVBox.setPadding(new Insets(5, 5, 5, 5));
        return dynamicVBox;
    }

    public VBox createEnvVariableVbox(String checkBoxLabelText, String typeLabel, String textFieldPrompt, DtoRange range) {
        VBox dynamicBox;
        Label label;
        CheckBox checkBox = new CheckBox(checkBoxLabelText);
        checkBox.setPadding(new Insets(5, 0, 0, 0));
        if((typeLabel.equalsIgnoreCase("float")) && range != null) {
            label = new Label("Type: " + typeLabel.toLowerCase()
                    + ", Range: " + range.getFrom() + "-" + range.getTo());
        }
        else {
            label = new Label("Type: " + typeLabel.toLowerCase());
        }
        Label errorLabel = new Label();
        if(typeLabel.equalsIgnoreCase("boolean")) {
            RadioButton trueButton = new RadioButton(TRUE_RADIO_BUTTON);
            RadioButton falseButton = new RadioButton(FALSE_RADIO_BUTTON);
            ToggleGroup toggleGroup = new ToggleGroup();
            trueButton.setToggleGroup(toggleGroup);
            falseButton.setToggleGroup(toggleGroup);

            dynamicBox = new VBox(checkBox, label, trueButton, falseButton, errorLabel);
            dynamicBox.setPadding(new Insets(5, 5, 5, 5));
        }
        else {
            TextField textField = new TextField();
            textField.setPromptText(textFieldPrompt);
            dynamicBox = new VBox(checkBox, label, textField, errorLabel);
            dynamicBox.setPadding(new Insets(5, 5, 5, 5));
        }
        return dynamicBox;
    }

    @FXML
    void clearButtonAction(ActionEvent event) {
        clearEntityText();
        clearEnvironmentVariable();
    }

    public void clearEntityText() {
        for (Node node : entitiesVbox.getChildren()) {
            if (node instanceof VBox) {
                VBox vBox = (VBox) node;
                for (Node innerNode : vBox.getChildren()) {
                    if (innerNode instanceof TextField) {
                        ((TextField) innerNode).clear();
                    }
                }
            }
        }

    }

    public void clearEnvironmentVariable() {
        for (Node node : envVariablesVbox.getChildren()) {
            if (node instanceof VBox) {
                VBox vBox = (VBox) node;
                for (Node innerNode : vBox.getChildren()) {
                    if(innerNode instanceof CheckBox) {
                        ((CheckBox) innerNode).setSelected(false);
                    }
                    else if (innerNode instanceof TextField) {
                        ((TextField) innerNode).clear();
                    }
                    else if(innerNode instanceof RadioButton) {
                        ((RadioButton) innerNode).setSelected(false);
                    }
                }
            }
        }

    }

    @FXML
    void startButtonAction(ActionEvent event) {
        boolean entityVal, envVal;
        boolean isNewSimulationFailed = false;
        // if the first time starting simulation with new xml - start thread
       if(mainController.getModel().getCurrSimulation().getSimulationStatus() == Status.CREATED){
           mainController.runTaskThreadPool();
       }
       if(mainController.getModel().getCurrSimulation().isSimulationSelected()){
           mainController.getModel().getCurrSimulation().setSimulationSelected(false);
       }

        // Option 1 - click start after loading new file from window 1 - no need to create new simulation

        // Option 2 - click start after been in window 3 and already running simulations
        // in this case we need to add new simulation in the manager and update the current ID to new simulation
        try {
            if (mainController.getModel().getCurrSimulation().getSimulationStatus() != Status.CREATED) {
                int currID = mainController.getModel().getSimulation().createSimulation(mainController.getCurrLoadedFileName());
                mainController.getModel().setCurrSimulationId(currID);
            }
        }
        catch (RuntimeException e){
            isNewSimulationFailed = true;
        }

        if(!isNewSimulationFailed) {
            entityVal = validateEntityPopulation();
            envVal = verifyEnvVariable();
            if (entityVal && envVal) {
                mainController.moveToResultsTab();
            }
            mainController.runSimulation();
        }
    }

    public boolean validateEntityPopulation() {
        boolean res = true;
        int sum = 0;
        int population;
        int limit = mainController.getModel().getDtoWorld().getGrid().getCols() * mainController.getModel().getDtoWorld().getGrid().getRows();
        for (Node node : entitiesVbox.getChildren()) {
            if (node instanceof VBox) {
                VBox vBox = (VBox) node;
                for (Node innerNode : vBox.getChildren()) {
                    if (innerNode instanceof TextField) {
                        try {
                            population = verifyPopulationIsNumber(((Label)vBox.getChildren().get(ENTITY_VBOX_ENTITY_NAME_INDEX)).getText(), ((TextField) innerNode).getText());
                            setErrorMessage(vBox, ENTITY_VBOX_ERROR_MESSAGE_INDEX, "", true);
                            res = true;
                            sum += population;
                        }
                        catch (NumberFormatException e) {
                            res = false;
                            setErrorMessage(vBox, ENTITY_VBOX_ERROR_MESSAGE_INDEX, e.getMessage(), false);
                        }
                    }
                }
            }
        }

        if(sum > limit) {
            res = false;
            mainController.showPopup("Population sum cannot exceed the limit of " + limit);
        }
        return res;
    }

    public int verifyPopulationIsNumber(String entityName, String population) throws NumberFormatException {
        try {
            int pop = Integer.parseInt(population);
            mainController.getModel().getCurrSimulation().setPopulationForEntity(entityName, pop);
            return pop;
        }
        catch (NumberFormatException e) {
            if(population.isEmpty()) {
                mainController.getModel().getCurrSimulation().setPopulationForEntity(entityName, 0);
                return 0;
            }
            else {
                throw new NumberFormatException("Population value should be numeric and an integer");
            }
        }
    }

    public boolean verifyEnvVariable() {
        boolean valid = true;
        boolean checked = false;
        String type = "";
        String envName = "";
        for (Node node : envVariablesVbox.getChildren()) {
            if (node instanceof VBox) {
                VBox vBox = (VBox) node;
                for (Node innerNode : vBox.getChildren()) {
                    if (innerNode instanceof CheckBox) {
                        envName = ((CheckBox) vBox.getChildren().get(ENVIRONMENT_VBOX_CHECKBOX_INDEX)).getText();
                        if (((CheckBox) innerNode).isSelected()) {
                            checked = true;
                        }
                    } else if(innerNode instanceof Label) {
                        if(((Label) vBox.getChildren().get(ENVIRONMENT_VBOX_TYPE_INDEX)).getText().contains("Type")) {
                            type = ((Label) vBox.getChildren().get(ENVIRONMENT_VBOX_TYPE_INDEX)).getText().substring(6);
                        }
                    }
                    else if (innerNode instanceof TextField) {
                        try {
                            //if the checkbox is checked and value should be numeric
                            if (checked) {
                                if((type.contains("float") || type.contains("decimal"))) {
                                    //remove decimal
                                    verifyEnvVariableIsNumber(envName, ((TextField) innerNode).getText());
                                    setErrorMessage(vBox, ENVIRONMENT_VBOX_TEXT_ERROR_MESSAGE_INDEX, "", true);
                                }
                                else if(type.contains("string")) {
                                    mainController.getModel().getCurrSimulation().setEnvVariable(envName, ((TextField) innerNode).getText());
                                    mainController.getModel().getCurrSimulation().setEnvOriginalVariable(envName, ((TextField) innerNode).getText());

                                }
                                valid = true;
                            }
                            //checkbox is not checked
                            else {
                                if(((TextField) innerNode).getText().isEmpty()) {
                                    //checkbox is not checked, and there's no user input --> generate value
                                    if(type.contains("float") || type.contains("decimal")){
                                        generateFloat(envName);
                                    }
                                    else if(type.equalsIgnoreCase("string")) {
                                        generateString(envName);
                                    }
                                    //delete error message, because value is not set anymore
                                    setErrorMessage(vBox, ENVIRONMENT_VBOX_TEXT_ERROR_MESSAGE_INDEX, "", true);
                                    valid = true;
                                }
                                //there is a value in the text field
                                else {
                                    valid = false;
                                    setErrorMessage(vBox, ENVIRONMENT_VBOX_TEXT_ERROR_MESSAGE_INDEX, "Please check the checkbox if you want to set a value", false);
                                }
                            }
                        } catch (NumberFormatException e) {
                            valid = false;
                            setErrorMessage(vBox, ENVIRONMENT_VBOX_TEXT_ERROR_MESSAGE_INDEX, e.getMessage(), false);

                        }
                    } else if (innerNode instanceof RadioButton) {

                        //if the checkbox is checked and value should be chosen in the radio button
                        if (checked && type.equalsIgnoreCase("boolean")) {
                            if (((RadioButton) vBox.getChildren().get(ENVIRONMENT_VBOX_RADIO_BUTTON_TRUE_INDEX)).isSelected()) {
                                mainController.getModel().getCurrSimulation().setEnvVariable(envName, TRUE_RADIO_BUTTON);
                                mainController.getModel().getCurrSimulation().setEnvOriginalVariable(envName, TRUE_RADIO_BUTTON);

                                setErrorMessage(vBox, ENVIRONMENT_VBOX_BOOLEAN_ERROR_MESSAGE_INDEX, "", true);
                                valid = true;

                            } else if (((RadioButton) vBox.getChildren().get(ENVIRONMENT_VBOX_RADIO_BUTTON_FALSE_INDEX)).isSelected()) {
                                mainController.getModel().getCurrSimulation().setEnvVariable(envName, FALSE_RADIO_BUTTON);
                                mainController.getModel().getCurrSimulation().setEnvOriginalVariable(envName, FALSE_RADIO_BUTTON);

                                setErrorMessage(vBox, ENVIRONMENT_VBOX_BOOLEAN_ERROR_MESSAGE_INDEX, "", true);
                                valid = true;
                            } else {//in case user didn't select any button
                                valid = false;
                                setErrorMessage(vBox, ENVIRONMENT_VBOX_BOOLEAN_ERROR_MESSAGE_INDEX, "checkbox is checked, please choose a value.", false);
                            }

                        }
                        else if(!checked) {
                            if(!(((RadioButton) vBox.getChildren().get(ENVIRONMENT_VBOX_RADIO_BUTTON_FALSE_INDEX)).isSelected())
                                    && !(((RadioButton) vBox.getChildren().get(ENVIRONMENT_VBOX_RADIO_BUTTON_TRUE_INDEX)).isSelected())) {
                                //checkbox is not checked, and there's no user input --> generate value
                                generateBoolean(envName);
                                //delete error message, because value is not set anymore
                                valid = true;
                                setErrorMessage(vBox, ENVIRONMENT_VBOX_BOOLEAN_ERROR_MESSAGE_INDEX, "", true);
                            }
                            //there is a value in the text field
                            else {
                                valid = false;
                                setErrorMessage(vBox, ENVIRONMENT_VBOX_BOOLEAN_ERROR_MESSAGE_INDEX, "Please check the checkbox if you want to set a value", false);

                            }
                        }

                    }
                }
                checked = false;
            }
        }
        for(PropertyInstance propertyInstance :  mainController.getModel().getCurrSimulation().getWorld().getEnvironment().getPropertyInstancesMap().values()) {
            System.out.println(mainController.getModel().getCurrSimulation().getWorld().getEnvironment().getEnvProperty(propertyInstance.getName()).getVal());

        }
        return valid;
    }

    public void verifyEnvVariableIsNumber(String envVarName, String value) throws NumberFormatException {
        try {
            mainController.getModel().getCurrSimulation().setEnvVariable(envVarName, value);
            mainController.getModel().getCurrSimulation().setEnvOriginalVariable(envVarName, value);

            //mainController.getModel().getSimulation().getWorld().setEnvValueByName(envVarName, value);
        }
        catch (NumberFormatException e) {
            if(value.isEmpty()) {
                throw new NumberFormatException("checkbox is checked, please enter a valid value.");

            }
            else {
                throw new NumberFormatException("Environment value should be numeric");
            }
        }

    }

    public void setErrorMessage(VBox vBox, int index, String message, boolean shouldRemove) {
        if (!shouldRemove) {
            vBox.getChildren().get(index).getStyleClass().add("color-error-label");

        }
        ((Label) vBox.getChildren().get(index)).setText(message);
    }


    public void generateFloat(String envPropName) {
        PropertyInstance currProp = mainController.getModel().getCurrSimulation().getEnvPropertyInstance(envPropName);
        if (currProp.getRange() != null) {
            Object random = ValueGeneratorFactory.createRandomFloat(currProp.getRange().getFrom(),
                    currProp.getRange().getTo()).generateValue();
//            currProp.setVal(ValueGeneratorFactory.createRandomFloat(currProp.getRange().getFrom(),
//                    currProp.getRange().getTo()).generateValue());

            currProp.setVal(random);
            currProp.setOriginalValueFromUser(random);

        }
        else {
            Object random = ValueGeneratorFactory.createRandomFloat(1.0f, 100.0f);
            currProp.setVal(random);
            currProp.setOriginalValueFromUser(random);
        }
    }

    public void generateBoolean(String envPropName) {
        PropertyInstance currProp = mainController.getModel().getCurrSimulation().getEnvPropertyInstance(envPropName);
        //PropertyInstance currProp = mainController.getModel().getSimulation().getWorld().getEnvironment().getEnvProperty(envPropName);
        Object random = ValueGeneratorFactory.createRandomBoolean().generateValue();
        //currProp.setVal(ValueGeneratorFactory.createRandomBoolean().generateValue());
        currProp.setVal(random);
        currProp.setOriginalValueFromUser(random);

    }

    public void generateString(String envPropName) {
        PropertyInstance currProp = mainController.getModel().getCurrSimulation().getEnvPropertyInstance(envPropName);
        //PropertyInstance currProp = mainController.getModel().getSimulation().getWorld().getEnvironment().getEnvProperty(envPropName);
        //currProp.setVal(ValueGeneratorFactory.createRandomString().generateValue());
        Object random = ValueGeneratorFactory.createRandomString().generateValue();
        currProp.setVal(random);
        currProp.setOriginalValueFromUser(random);

    }

    public void populateTabFromRerunSimulation() {
        setOriginalEntityPopulation();
        setOriginalEnvVariable();
    }

    public void setOriginalEntityPopulation() {
        int population;
        for (Node node : entitiesVbox.getChildren()) {
            if (node instanceof VBox) {
                VBox vBox = (VBox) node;
                for (Node innerNode : vBox.getChildren()) {
                    if (innerNode instanceof TextField) {
                        ((TextField)innerNode).clear();
                        population = mainController.getModel().getCurrSimulation().getPopulationForEntity(((Label)vBox.getChildren().get(ENTITY_VBOX_ENTITY_NAME_INDEX)).getText());
                        ((TextField) innerNode).setText(String.valueOf(mainController.getModel().getCurrSimulation().getPopulationForEntity(((Label)vBox.getChildren().get(ENTITY_VBOX_ENTITY_NAME_INDEX)).getText())));

                    }
                }
            }
        }
    }

    public void setOriginalEnvVariable() {
        String envName = "";
        for (Node node : envVariablesVbox.getChildren()) {
            if (node instanceof VBox) {
                VBox vBox = (VBox) node;
                for (Node innerNode : vBox.getChildren()) {
                    if (innerNode instanceof CheckBox) {
                        envName = ((CheckBox) vBox.getChildren().get(ENVIRONMENT_VBOX_CHECKBOX_INDEX)).getText();
                        ((CheckBox) innerNode).setSelected(true);
                    } else if (innerNode instanceof TextField) {
                        ((TextField) innerNode).clear();
                        ((TextField) innerNode).setText(String.valueOf(mainController.getModel().getCurrSimulation().getEnvPropertyInstance(envName).getOriginalValueFromUser()));

                    } else if (innerNode instanceof RadioButton) {
                        String choice = String.valueOf(mainController.getModel().getCurrSimulation().getEnvPropertyInstance(envName).getOriginalValueFromUser());
                        if (choice.equals("true"))
                            ((RadioButton) vBox.getChildren().get(ENVIRONMENT_VBOX_RADIO_BUTTON_TRUE_INDEX)).setSelected(true);
                        else
                            ((RadioButton) vBox.getChildren().get(ENVIRONMENT_VBOX_RADIO_BUTTON_FALSE_INDEX)).setSelected(true);
                    }
                }
            }
        }
    }
}

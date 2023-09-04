package subcomponents.actions.Calculation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CalculateComponentController {
    @FXML
    private Label CalculateTypeLabel;
    @FXML
    private Label CalculatePrimaryLabel;
    @FXML
    private Label CalculateSecondaryExistLabel;
    @FXML
    private Label CalculateSecondaryEntityLabel;
    @FXML
    private Label CalculateOperatorLabel;
    @FXML
    private Label CalculateFirstArgumentLabel;
    @FXML
    private Label CalculateSecondArgumentLabel;
    @FXML
    private Label CalculateResultPropertyLabel;

    public void setTxtTypeLabel(String input){
        CalculateTypeLabel.setText(input);
    }
    public void setTxtPrimaryLabel(String input){
        CalculatePrimaryLabel.setText(input);
    }
    public void setTxtSecondaryExistLabel(String input){
        CalculateSecondaryExistLabel.setText(input);
    }
    public void setTxtSecondaryLabel(String input){
        CalculateSecondaryEntityLabel.setText(input);
    }
    public void setTxtOperatorLabel(String input){
        CalculateOperatorLabel.setText(input);
    }
    public void setTxtFirstArgumentLabel(String input){
        CalculateFirstArgumentLabel.setText(input);
    }
    public void setTxtSecondArgumentLabel(String input){
        CalculateSecondArgumentLabel.setText(input);
    }
    public void setTxtResultPropertyLabel(String input){
        CalculateResultPropertyLabel.setText(input);
    }
}

package user.subcomponents.actions.Condition.single;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SingleConditionComponentController {
    @FXML
    private Label SingleConditionTypeLabel;
    @FXML
    private Label SingleConditionPrimaryLabel;
    @FXML
    private Label SingleConditionSecondaryExistLabel;
    @FXML
    private Label SingleConditionSecondaryEntityLabel;
    @FXML
    private Label SingleConditionPropertyLabel;
    @FXML
    private Label SingleConditionOperatorLabel;
    @FXML
    private Label SingleConditionValueLabel;
    @FXML
    private Label ThenConditionSingle;
    @FXML
    private Label ElseConditionSingle;

    public void setTxtTypeLabel(String input){
        SingleConditionTypeLabel.setText(input);
    }
    public void setTxtPrimaryLabel(String input){
        SingleConditionPrimaryLabel.setText(input);
    }
    public void setTxtSecondaryExistLabel(String input){
        SingleConditionSecondaryExistLabel.setText(input);
    }
    public void setTxtSecondaryLabel(String input){
        SingleConditionSecondaryEntityLabel.setText(input);
    }
    public void setTxtPropertyLabel(String input){
        SingleConditionPropertyLabel.setText(input);
    }
    public void setTxtOperatorLabel(String input){
        SingleConditionOperatorLabel.setText(input);
    }
    public void setTxtValueLabel(String input){SingleConditionValueLabel.setText(input);}
    public void setTxtThenConditionLabel(String input){
        ThenConditionSingle.setText(input);
    }
    public void setTxtElseConditionLabel(String input){
        ElseConditionSingle.setText(input);
    }

}

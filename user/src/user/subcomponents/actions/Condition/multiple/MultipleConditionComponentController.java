package user.subcomponents.actions.Condition.multiple;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MultipleConditionComponentController {
    @FXML
    private Label MultipleConditionTypeLabel;
    @FXML
    private Label MultipleConditionPrimaryLabel;
    @FXML
    private Label MultipleConditionSecondaryExistLabel;
    @FXML
    private Label MultipleConditionSecondaryEntityLabel;
    @FXML
    private Label MultipleConditionLogicalLable;
    @FXML
    private Label MultipleConditionNumberLabel;
    @FXML
    private Label ThenConditionMulti;
    @FXML
    private Label ElseConditionMulti;

    public void setTxtTypeLabel(String input){
        MultipleConditionTypeLabel.setText(input);
    }
    public void setTxtPrimaryLabel(String input){
        MultipleConditionPrimaryLabel.setText(input);
    }
    public void setTxtSecondaryExistLabel(String input){
        MultipleConditionSecondaryExistLabel.setText(input);
    }
    public void setTxtSecondaryLabel(String input){
        MultipleConditionSecondaryEntityLabel.setText(input);
    }
    public void setTxtLogicalLabel(String input){
        MultipleConditionLogicalLable.setText(input);
    }
    public void setTxtConditionNumberLabel(String input){
        MultipleConditionNumberLabel.setText(input);
    }
    public void setTxtThenConditionLabel(String input){
        ThenConditionMulti.setText(input);
    }
    public void setTxtElseConditionLabel(String input){
        ElseConditionMulti.setText(input);
    }

}

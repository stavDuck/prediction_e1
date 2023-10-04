package user.subcomponents.actions.Set;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SetComponentController {
    @FXML
    private Label SetTypeLabel;
    @FXML
    private Label SetPrimaryLabel;
    @FXML
    private Label SetSecondaryExistLabel;
    @FXML
    private Label SetSecondaryEntityLabel;
    @FXML
    private Label SetPropertyLabel;
    @FXML
    private Label SetValueLabel;

    public void setTxtTypeLabel(String input){
        SetTypeLabel.setText(input);
    }
    public void setTxtPrimaryLabel(String input){
        SetPrimaryLabel.setText(input);
    }
    public void setTxtSecondaryExistLabel(String input){
        SetSecondaryExistLabel.setText(input);
    }
    public void setTxtSecondaryLabel(String input){
        SetSecondaryEntityLabel.setText(input);
    }
    public void setTxtPropertyLabel(String input){
        SetPropertyLabel.setText(input);
    }
    public void setTxtValueLable(String input){
        SetValueLabel.setText(input);
    }

}

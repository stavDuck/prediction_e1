package subcomponents.actions.Replace;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ReplaceComponentController {
    @FXML
    private Label ReplaceTypeLabel;
    @FXML
    private Label ReplacePrimaryLabel;
    @FXML
    private Label ReplaceSecondaryExistLabel;
    @FXML
    private Label ReplaceSecondaryEntityLabel;
    @FXML
    private Label ReplaceCreateEntityLabel;
    @FXML
    private Label ReplaceModeLabel;

    public void setTxtTypeLabel(String input){
       ReplaceTypeLabel.setText(input);
    }
    public void setTxtPrimaryLabel(String input){
        ReplacePrimaryLabel.setText(input);
    }
    public void setTxtSecondaryExistLabel(String input){
        ReplaceSecondaryExistLabel.setText(input);
    }
    public void setTxtSecondaryLabel(String input){
        ReplaceSecondaryEntityLabel.setText(input);
    }
    public void setTxtCreateEntityLable(String input){
        ReplaceCreateEntityLabel.setText(input);
    }
    public void setTxtModeLable(String input){
        ReplaceModeLabel.setText(input);
    }
}

package admin.subcomponents.actions.Increase;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class IncreaseComponentController {
    @FXML
    private Label IncreaseTypeLabel;
    @FXML
    private Label IncreasePrimaryLabel;
    @FXML
    private Label IncreaseSecondaryExistLabel;
    @FXML
    private Label IncreaseSecondaryEntityLabel;
    @FXML
    private Label IncreasePropertyNameLabel;
    @FXML
    private Label IncreaseByLabel;

    public void setTxtTypeLabel(String input){
        IncreaseTypeLabel.setText(input);
    }
    public void setTxtPrimaryLabel(String input){
        IncreasePrimaryLabel.setText(input);
    }
    public void setTxtSecondaryExistLabel(String input){
        IncreaseSecondaryExistLabel.setText(input);
    }
    public void setTxtSecondaryLabel(String input){
        IncreaseSecondaryEntityLabel.setText(input);
    }
    public void setTxtPropertyLabel(String input){
        IncreasePropertyNameLabel.setText(input);
    }
    public void setTxtByLabel(String iput){
        IncreaseByLabel.setText(iput);
    }
}

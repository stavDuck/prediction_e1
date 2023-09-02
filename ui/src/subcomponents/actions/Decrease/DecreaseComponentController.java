package subcomponents.actions.Decrease;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DecreaseComponentController {
    @FXML
    private Label DecreaseTypeLabel;
    @FXML
    private Label DecreasePrimaryLabel;
    @FXML
    private Label DecreaseSecondaryExistLabel;
    @FXML
    private Label DecreaseSecondaryEntityLabel;
    @FXML
    private Label DecreasePropertyNameLabel;
    @FXML
    private Label DecreaseByLabel;

    public void setTxtTypeLabel(String input){
        DecreaseTypeLabel.setText(input);
    }
    public void setTxtPrimaryLabel(String input){
        DecreasePrimaryLabel.setText(input);
    }
    public void setTxtSecondaryExistLabel(String input){
        DecreaseSecondaryExistLabel.setText(input);
    }
    public void setTxtSecondaryLabel(String input){
        DecreaseSecondaryEntityLabel.setText(input);
    }
    public void setTxtPropertyLabel(String input){
        DecreasePropertyNameLabel.setText(input);
    }
    public void setTxtByLabel(String iput){
        DecreaseByLabel.setText(iput);
    }
}

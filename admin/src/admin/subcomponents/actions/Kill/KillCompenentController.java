package admin.subcomponents.actions.Kill;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KillCompenentController {

    @FXML
    private Label KillTypeLabel;

    @FXML
    private Label KillPrimaryLabel;

    @FXML
    private Label KillSecondaryExistLabel;

    @FXML
    private Label KillSecondaryEntityLabel;

    public void setTxtTypeLabel(String input){
        KillTypeLabel.setText(input);
    }
    public void setTxtPrimaryLabel(String input){
        KillPrimaryLabel.setText(input);
    }
    public void setTxtSecondaryExistLabel(String input){
        KillSecondaryExistLabel.setText(input);
    }
    public void setTxtSecondaryLabel(String input){
        KillSecondaryEntityLabel.setText(input);
    }

}

package subcomponents.actions.Proximity;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProximityComponentController {
    @FXML
    private Label ProximityTypeLabel;
    @FXML
    private Label ProximityPrimaryLabel;
    @FXML
    private Label ProximitySecondaryExistLabel;
    @FXML
    private Label ProximitySecondaryEntityLabel;
    @FXML
    private Label ProximityTargetEntityLabel;
    @FXML
    private Label ProximityCircleDepthLabel;


    public void setTxtTypeLabel(String input){
        ProximityTypeLabel.setText(input);
    }
    public void setTxtPrimaryLabel(String input){
        ProximityPrimaryLabel.setText(input);
    }
    public void setTxtSecondaryExistLabel(String input){
        ProximitySecondaryExistLabel.setText(input);
    }
    public void setTxtSecondaryLabel(String input){
        ProximitySecondaryEntityLabel.setText(input);
    }
    public void setTxtTargetEntityLabel(String input){
        ProximityTargetEntityLabel.setText(input);
    }
    public void setTxtDepthLabel(String input){
        ProximityCircleDepthLabel.setText(input);
    }
}

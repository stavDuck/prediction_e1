package admin.subcomponents.tabs.allocations;

import admin.subcomponents.app.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class AllocationComponentController {

    @FXML
    private TableView<?> requsetAdminTable;
    private AppController mainController;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }


}

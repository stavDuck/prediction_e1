package subcomponents.tabs.details;

import dto.Dto;
import dto.entity.DtoEntity;
import dto.env.DtoEnv;
import dto.property.DtoProperty;
import dto.rule.DtoRule;
import dto.termination.DtoTermination;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import subcomponents.app.AppController;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class DetailsComponentController {
    private final static String ENTITIES_TITLE = "Entities";
    private final static String ENVS_TITLE = "Environment Variables";
    private final static String RULES_TITLE = "Rules List";
    private final static String TERMINATION_TITLE = "Termination Conditions";
    private final static String ROOT_TITEL = "Simulation's Information";

    private AppController mainController;
    @FXML
    private GridPane gridPanelDetailsView;

    @FXML
    private Label informationDetailsTitle;

    @FXML
    private TreeView<String> treeViewInformation;

    @FXML
    private TextArea informationDetailsBody;



    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void loadDetailsView(){
        Dto dto = mainController.getDtoWorld();
        Collection<DtoEntity> dtoEntityLst = dto.getEntities().values();
        Collection<DtoEnv> dtoEnvLst = dto.getEnvs().values();
        Collection<DtoRule> dtoRuleLst = dto.getRules().values();
        DtoTermination dtoTermination = dto.getTermination();

        // set the tree root
        TreeItem<String> rootItem = new TreeItem<>("Simulation's Information");

        // create view of entities by entities names
        if(dtoEntityLst!= null && !dtoEntityLst.isEmpty()) {
            TreeItem<String> branchItemEntities = new TreeItem<>(ENTITIES_TITLE);
            // add leafs by entities names
            for (DtoEntity curr : dtoEntityLst){
                branchItemEntities.getChildren().add(new TreeItem<>(curr.getEntityName()));
            }
            rootItem.getChildren().add(branchItemEntities);
        }

        // create view of env values by names
        if(dtoEnvLst != null && !dtoEnvLst.isEmpty()){
            TreeItem<String> branchItemEnvs = new TreeItem<>(ENVS_TITLE);
            // add leafs by env vars names
            for(DtoEnv curr : dtoEnvLst){
                branchItemEnvs.getChildren().add(new TreeItem<>(curr.getEnvName()));
            }
            rootItem.getChildren().add(branchItemEnvs);
        }

        // create view of rules list by rule names
        if(dtoRuleLst != null && !dtoRuleLst.isEmpty()){
            TreeItem<String> branchItemRules = new TreeItem<>(RULES_TITLE);
            // add leafs by env vars names
            for(DtoRule curr : dtoRuleLst){
                branchItemRules.getChildren().add(new TreeItem<>(curr.getName()));
            }
            rootItem.getChildren().add(branchItemRules);
        }

        // create view of Termination conidtions
        if(dtoTermination != null){
            TreeItem<String> branchItemTermination = new TreeItem<>(TERMINATION_TITLE);
            branchItemTermination.getChildren().addAll(new TreeItem<>("By Ticks"),new TreeItem<>("By Time"));
            rootItem.getChildren().add(branchItemTermination);
        }

        treeViewInformation.setRoot(rootItem);
    }

    @FXML
    void selectItem(MouseEvent event) {
        String txt = "";
        Dto dto = mainController.getDtoWorld();
        TreeItem<String> item = treeViewInformation.getSelectionModel().getSelectedItem();

        // informationDetailsTitle --> title
        //informationDetailsBody --> body

        if(item != null && !item.getValue().equals(ROOT_TITEL)){
            // reset info in screen
            informationDetailsTitle.setText("");
            informationDetailsBody.clear();
            informationDetailsTitle.setText(item.getValue());

            String ifoType = item.getParent().getValue();
            switch (ifoType){
                case ENTITIES_TITLE:
                    DtoEntity dtoEntity = dto.getEntities().get(item.getValue());
                    txt += "Entity Properties: \n" +
                           "******************\n";
                 for(DtoProperty curr : dtoEntity.getPropertyList()){
                     txt += "Property Name: " + curr.getName() + "\n";
                     txt += "Property Type: " + curr.getType() + "\n";
                     if(curr.getRange() != null){
                         txt += "Property Range from: " + curr.getRange().getFrom() + ", to: " + curr.getRange().getTo() + "\n";
                     }
                     txt +="\n";
                 }
                    break;
                case ENVS_TITLE:
                    DtoEnv dtoEnv = dto.getEnvs().get(item.getValue());
                    txt += "Variable Type: " + dtoEnv.getEnvType() + "\n";
                    if(dtoEnv.getEnvRange() != null){
                        txt += "Property Range from: " + dtoEnv.getEnvRange().getFrom() + ", to: " + dtoEnv.getEnvRange().getTo() + "\n";
                    }
                    break;
                case RULES_TITLE:
                    break;
                case TERMINATION_TITLE:
                    if (item.getValue().indexOf("Ticks") != -1){
                        txt += "End Condition By Ticks: " + dto.getTermination().getByTick();
                    }
                    else{
                        txt += "End Condition By Seconds: " + dto.getTermination().getBySeconds();
                    }
                    break;
            }

            informationDetailsBody.setText(txt);
        }
    }
}

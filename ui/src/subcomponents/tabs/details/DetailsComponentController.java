package subcomponents.tabs.details;

import dto.Dto;
import dto.entity.DtoEntity;
import dto.env.DtoEnv;
import dto.property.DtoProperty;
import dto.rule.Action.DtoAbstractAction;
import dto.rule.Action.DtoIncrease;
import dto.rule.DtoRule;
import dto.termination.DtoTermination;
import engine.activation.Activation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import subcomponents.actions.Increase.IncreaseComponentController;
import subcomponents.app.AppController;
import subcomponents.common.ResourcesConstants;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class DetailsComponentController {
    private final static String ENTITIES_TITLE = "Entities";
    private final static String ENVS_TITLE = "Environment Variables";
    private final static String RULES_TITLE = "Rules List";
    private final static String RULES_ACTIVATION = "Activation";
    private final static String RULES_ACTIONS = "Actions";
    private final static String TERMINATION_TITLE = "Termination Conditions";
    private final static String ROOT_TITEL = "Simulation's Information";
    private final static String INCREASE = "increase";
    private final static String DECREASE = "decrease";
    private final static String CALCULATION = "calculation";
    private final static String CONDITION = "condition";
    private final static String SET = "set";
    private final static String KILL = "kill";
    private final static String REPLACE = "replace";
    private final static String PROXIMITY = "proximity";

    private AppController mainController;
    @FXML
    private GridPane gridPanelDetailsView;

    @FXML
    private Label informationDetailsTitle;

    @FXML
    private TreeView<String> treeViewInformation;

    @FXML
    private VBox informationDetailsBody;


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
                TreeItem<String> branchItemRuleName = new TreeItem<>(curr.getName());

                TreeItem<String> branchItemRuleActions = new TreeItem<>(RULES_ACTIONS);
                // create activation
                TreeItem<String> branchItemRuleActivation = new TreeItem<>(RULES_ACTIVATION);

                // create actions for curr rule
                for(DtoAbstractAction currAction : curr.getAction()){
                    branchItemRuleActions.getChildren().add(new TreeItem<>(currAction.getType()));
                }


                //- rules
                // -- rule name
                // --- actions
                // ---- increase
                // ---- decrease
                // --- activation
                branchItemRuleName.getChildren().add(branchItemRuleActions);
                branchItemRuleName.getChildren().add(branchItemRuleActivation);
                branchItemRules.getChildren().add(branchItemRuleName);

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


        if(item != null && !item.getValue().equals(ROOT_TITEL)){
           boolean isValueRuleAction = false;
            int indexChildInRuleLst;
            DtoRule dtoRule;

            // reset info in screen
            informationDetailsTitle.setText("");
            // clear details body
            informationDetailsBody.getChildren().clear();
            informationDetailsBody.getChildren().removeAll();
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
                case TERMINATION_TITLE:
                    if (item.getValue().indexOf("Ticks") != -1){
                        txt += "End Condition By Ticks: " + dto.getTermination().getByTick();
                    }
                    else{
                        txt += "End Condition By Seconds: " + dto.getTermination().getBySeconds();
                    }
                    break;
                case RULES_ACTIONS:
                    isValueRuleAction = true;
                    handleRuleAction(dto, item);
                    break;
            }

            if(item.getValue().equals(RULES_ACTIVATION)) {
                indexChildInRuleLst = getRuleIndexBySelectedActivation(item);
                dtoRule = (new ArrayList<DtoRule>(dto.getRules().values())).get(indexChildInRuleLst);
                txt += "Activation by Ticks: " + dtoRule.getActivation().getTicks() + "\n" +
                        "Activation by Probability: " + dtoRule.getActivation().getProbability();
            }

            // if not rule action need to set regular txt in body
            if(!isValueRuleAction) {
                informationDetailsBody.getChildren().add(new Label(txt));
            }
        }
    }

    private void handleRuleAction(Dto dto, TreeItem<String> item) {
        int indexChildInRuleLst;
        int indexChildActionLst;
        DtoRule dtoRule;
        DtoAbstractAction dtoAction;

        switch (item.getValue()){
            case INCREASE:
                indexChildInRuleLst = getRuleIndexBySelectedRuleAction(item);
                dtoRule = (new ArrayList<DtoRule>(dto.getRules().values())).get(indexChildInRuleLst);
                indexChildActionLst = getActionIndexBySelectedRuleAction(item);
                dtoAction = dtoRule.getAction().get(indexChildActionLst);
                addIncreaseInformationToBody(dtoAction);
                break;
            case DECREASE:
                indexChildInRuleLst = getRuleIndexBySelectedRuleAction(item);
                dtoRule = (new ArrayList<DtoRule>(dto.getRules().values())).get(indexChildInRuleLst);
                indexChildActionLst = getActionIndexBySelectedRuleAction(item);
                dtoAction = dtoRule.getAction().get(indexChildActionLst);
                //addDecreaseInformationToBody(dtoAction);
                break;
            case CALCULATION:
                indexChildInRuleLst = getRuleIndexBySelectedRuleAction(item);
                dtoRule = (new ArrayList<DtoRule>(dto.getRules().values())).get(indexChildInRuleLst);
                indexChildActionLst = getActionIndexBySelectedRuleAction(item);
                dtoAction = dtoRule.getAction().get(indexChildActionLst);
                // addCalculationInformationToBody(dtoAction);
                break;
            case CONDITION:
                break;
            case SET:
                break;
            case KILL:
                break;
            case REPLACE:
                break;
            case PROXIMITY:
                break;

        }
    }

    private void addIncreaseInformationToBody(DtoAbstractAction dtoAction) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(ResourcesConstants.INCREACE_FXML_INCLUDE_RESOURCE);
            loader.setLocation(url);
            loader.setRoot(new VBox());
            Node increaseTile = loader.load();
            IncreaseComponentController increaseController = loader.getController();

            // set information
            increaseController.setTxtTypeLabel(dtoAction.getType());
            increaseController.setTxtPrimaryLabel(dtoAction.getPrimaryEntity());
            increaseController.setTxtSecondaryExistLabel(dtoAction.isSecondaryExist() == true ? "Yes" : "No");
            increaseController.setTxtSecondaryLabel(dtoAction.getSecondaryEntity());
            increaseController.setTxtPropertyLabel(((DtoIncrease)dtoAction).getProperty());
            increaseController.setTxtPropertyLabel(((DtoIncrease)dtoAction).getByExpression());

            informationDetailsBody.getChildren().add(increaseTile);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private int getRuleIndexBySelectedActivation(TreeItem<String> item) {
        TreeItem<String> selectedRule = item.getParent(); // get rule name item
        TreeItem<String> RulesRoot = item.getParent().getParent(); // get rules root
        int counter = 0;
        boolean isFound = false;

        // go over all the rules in the tree and count what is the index of the selected rule
        for(TreeItem<String> currRule : RulesRoot.getChildren()){
            if(selectedRule == currRule){
                isFound = true;
            }
            else if(selectedRule != currRule && !isFound){
                counter++;
            }
        }
        return counter;
    }

    private int getRuleIndexBySelectedRuleAction(TreeItem<String> item){
        TreeItem<String> selectedRule = item.getParent().getParent(); // first parent - actions, secont parent - rule name item
        TreeItem<String> RulesRoot = item.getParent().getParent().getParent(); // get rules root
        int counter = 0;
        boolean isFound = false;

        for(TreeItem<String> currRule : RulesRoot.getChildren()) {
            if(selectedRule == currRule){
                isFound = true;
            }
            else if(selectedRule != currRule && !isFound){
                counter++;
            }
        }
        return counter;
    }

    private int getActionIndexBySelectedRuleAction(TreeItem<String> item){
        TreeItem<String> actionsLstTree = item.getParent();
        int counter = 0;
        boolean isFound = false;

        for(TreeItem<String> currAction : actionsLstTree.getChildren()) {
            if(currAction == item){
                isFound = true;
            }
            else if(item != currAction && !isFound){
                counter++;
            }
        }
        return counter;
    }
}

package admin.subcomponents.tabs.details;

import admin.subcomponents.actions.Calculation.CalculateComponentController;
import admin.subcomponents.actions.Condition.multiple.MultipleConditionComponentController;
import admin.subcomponents.actions.Condition.single.SingleConditionComponentController;
import admin.subcomponents.actions.Decrease.DecreaseComponentController;
import admin.subcomponents.actions.Increase.IncreaseComponentController;
import admin.subcomponents.actions.Kill.KillCompenentController;
import admin.subcomponents.actions.Proximity.ProximityComponentController;
import admin.subcomponents.actions.Replace.ReplaceComponentController;
import admin.subcomponents.actions.Set.SetComponentController;
import admin.subcomponents.common.ResourcesConstants;
import dto.Dto;
import dto.entity.DtoEntity;
import dto.env.DtoEnv;
import dto.property.DtoProperty;
import dto.rule.Action.*;
import dto.rule.DtoRule;
import dto.termination.DtoTermination;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import admin.subcomponents.app.AppController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;


public class DetailsComponentController {
    private final static String ENTITIES_TITLE = "Entities";
    private final static String ENVS_TITLE = "Environment Variables";
    private final static String RULES_TITLE = "Rules List";
    private final static String RULES_ACTIVATION = "Activation";
    private final static String RULES_ACTIONS = "Actions";
    private final static String TERMINATION_TITLE = "Termination Conditions";
    private final static String GRID_TITLE = "Grid";

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

        // clear if already exist in open new file
        if (treeViewInformation.getRoot() != null &&
                treeViewInformation.getRoot().getChildren() != null){
            treeViewInformation.getRoot().getChildren().clear();
        }
        if(informationDetailsBody.getChildren() != null){
            informationDetailsBody.getChildren().clear();
            informationDetailsTitle.setText("");
        }


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
        // create view of Grid
        TreeItem<String> branchItemGrid = new TreeItem<>(GRID_TITLE);
        rootItem.getChildren().add(branchItemGrid);

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
            //informationDetailsBody.getChildren().clear();
           removeAllChildrenFromBody(informationDetailsBody);


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
            else if(item.getValue().equals(GRID_TITLE)){
                txt += "Rows: " + dto.getGrid().getRows() + ", Columns: " + dto.getGrid().getCols();
            }

            // if not rule action need to set regular txt in body
            if(!isValueRuleAction) {
                informationDetailsBody.getChildren().add(new Label(txt));
            }
        }
    }

    private void removeAllChildrenFromBody(VBox informationDetailsBody) {
        while(informationDetailsBody.getChildren().size() > 0){
            informationDetailsBody.getChildren().remove(0);
        }
    }

    private void handleRuleAction(Dto dto, TreeItem<String> item) {
        int indexChildInRuleLst;
        int indexChildActionLst;
        DtoRule dtoRule;
        DtoAbstractAction dtoAction;

        indexChildInRuleLst = getRuleIndexBySelectedRuleAction(item);
        dtoRule = (new ArrayList<DtoRule>(dto.getRules().values())).get(indexChildInRuleLst);
        indexChildActionLst = getActionIndexBySelectedRuleAction(item);
        dtoAction = dtoRule.getAction().get(indexChildActionLst);

        switch (item.getValue()){
            case INCREASE:
                addIncreaseInformationToBody(dtoAction);
                break;
            case DECREASE:
                addDecreaseInformationToBody(dtoAction);
                break;
            case CALCULATION:
                addCalculationInformationToBody(dtoAction);
                break;
            case CONDITION:
                addConditionInformationToBody(dtoAction);
                break;
            case SET:
                addSetInformationToBody(dtoAction);
                break;
            case KILL:
                addKillInformationToBody(dtoAction);
                break;
            case REPLACE:
                addReplaceInformationToBody(dtoAction);
                break;
            case PROXIMITY:
                addProximityInformationToBody(dtoAction);
                break;
        }
    }

    private void addProximityInformationToBody(DtoAbstractAction dtoAction) {
        try{
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(ResourcesConstants.PROXIMITY_FXML_INCLUDE_RESOURCE);
            loader.setLocation(url);

                //loader.setRoot(new VBox());

            Node tile = loader.load();
            ProximityComponentController tileController = loader.getController();

            // set information
            tileController.setTxtTypeLabel(dtoAction.getType());
            tileController.setTxtPrimaryLabel(dtoAction.getPrimaryEntity());
            tileController.setTxtSecondaryExistLabel(dtoAction.isSecondaryExist() == true ? "Yes" : "No");
            tileController.setTxtSecondaryLabel(dtoAction.getSecondaryEntity());
            tileController.setTxtTargetEntityLabel(((DtoProximity)dtoAction).getTargetEntity());
            tileController.setTxtDepthLabel(((DtoProximity)dtoAction).getEnvDepthOf());

            informationDetailsBody.getChildren().add(tile);


        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addReplaceInformationToBody(DtoAbstractAction dtoAction) {
        try{
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(ResourcesConstants.KILL_FXML_INCLUDE_RESOURCE);
            loader.setLocation(url);
           // loader.setRoot(new VBox());
            Node tile = loader.load();
            ReplaceComponentController tileController = loader.getController();

            // set information
            tileController.setTxtTypeLabel(dtoAction.getType());
            tileController.setTxtPrimaryLabel(dtoAction.getPrimaryEntity());
            tileController.setTxtSecondaryExistLabel(dtoAction.isSecondaryExist() == true ? "Yes" : "No");
            tileController.setTxtSecondaryLabel(dtoAction.getSecondaryEntity());
            tileController.setTxtCreateEntityLable(((DtoReplace)dtoAction).getCreateEntity());
            tileController.setTxtModeLable(((DtoReplace)dtoAction).getMode());

            informationDetailsBody.getChildren().add(tile);
           // loader.setRoot(null);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addKillInformationToBody(DtoAbstractAction dtoAction) {
        try{
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(ResourcesConstants.KILL_FXML_INCLUDE_RESOURCE);
            loader.setLocation(url);
            //loader.setRoot(new VBox());
            Node tile = loader.load();
            KillCompenentController tileController = loader.getController();

            // set information
            tileController.setTxtTypeLabel(dtoAction.getType());
            tileController.setTxtPrimaryLabel(dtoAction.getPrimaryEntity());
            tileController.setTxtSecondaryExistLabel(dtoAction.isSecondaryExist() == true ? "Yes" : "No");
            tileController.setTxtSecondaryLabel(dtoAction.getSecondaryEntity());

            informationDetailsBody.getChildren().add(tile);
            //loader.setRoot(null);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addSetInformationToBody(DtoAbstractAction dtoAction) {
        try{
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(ResourcesConstants.SET_FXML_INCLUDE_RESOURCE);
            loader.setLocation(url);
            //loader.setRoot(new VBox());
            Node tile = loader.load();
            SetComponentController tileController = loader.getController();

            // set information
            tileController.setTxtTypeLabel(dtoAction.getType());
            tileController.setTxtPrimaryLabel(dtoAction.getPrimaryEntity());
            tileController.setTxtSecondaryExistLabel(dtoAction.isSecondaryExist() == true ? "Yes" : "No");
            tileController.setTxtSecondaryLabel(dtoAction.getSecondaryEntity());
            tileController.setTxtPropertyLabel(((DtoSet)dtoAction).getProperty());
            tileController.setTxtValueLable(((DtoSet)dtoAction).getNewValue());

            informationDetailsBody.getChildren().add(tile);
           // loader.setRoot(null);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addConditionInformationToBody(DtoAbstractAction dtoAction) {
        String singularity = ((DtoCondition)dtoAction).getSingularity();
        URL url = null;
        try{
            FXMLLoader loader = new FXMLLoader();
           if(singularity.equals("single")){
               url = getClass().getResource(ResourcesConstants.SINGEL_CONDITION_FXML_INCLUDE_RESOURCE);
           }
           else{
               url = getClass().getResource(ResourcesConstants.MULTIPLE_CONDITION_FXML_INCLUDE_RESOURCE);
           }

            loader.setLocation(url);
            //loader.setRoot(new VBox());
            Node tile = loader.load();

            if(singularity.equals("single")){
                SingleConditionComponentController tileController = loader.getController();
                // set information
                tileController.setTxtTypeLabel(dtoAction.getType());
                tileController.setTxtPrimaryLabel(dtoAction.getPrimaryEntity());
                tileController.setTxtSecondaryExistLabel(dtoAction.isSecondaryExist() == true ? "Yes" : "No");
                tileController.setTxtSecondaryLabel(dtoAction.getSecondaryEntity());
                tileController.setTxtPropertyLabel((( (DtoCondition) dtoAction).getDtoSingleCondition()).getProperty());
                tileController.setTxtOperatorLabel((( (DtoCondition) dtoAction).getDtoSingleCondition()).getOperator());
                tileController.setTxtValueLabel((( (DtoCondition) dtoAction).getDtoSingleCondition()).getValue());
                Integer thenConition = ((DtoCondition)dtoAction).getThenConditionsNumber();
                Integer elseCondition = ((DtoCondition)dtoAction).getElseConditionsNumber();
                tileController.setTxtThenConditionLabel(thenConition.toString());
                tileController.setTxtThenConditionLabel(elseCondition.toString());
            }
            else{
                MultipleConditionComponentController tileController = loader.getController();
                // set information
                tileController.setTxtTypeLabel(dtoAction.getType());
                tileController.setTxtPrimaryLabel(dtoAction.getPrimaryEntity());
                tileController.setTxtSecondaryExistLabel(dtoAction.isSecondaryExist() == true ? "Yes" : "No");
                tileController.setTxtSecondaryLabel(dtoAction.getSecondaryEntity());
                tileController.setTxtLogicalLabel((( (DtoCondition) dtoAction).getDtoMultipleCondition()).getLogic());
                Integer number = ((( (DtoCondition) dtoAction).getDtoMultipleCondition()).getConditionNumber());
                tileController.setTxtConditionNumberLabel(number.toString());
                Integer thenConition = ((DtoCondition)dtoAction).getThenConditionsNumber();
                Integer elseCondition = ((DtoCondition)dtoAction).getElseConditionsNumber();
                tileController.setTxtThenConditionLabel(thenConition.toString());
                tileController.setTxtThenConditionLabel(elseCondition.toString());
            }

            informationDetailsBody.getChildren().add(tile);
            //loader.setRoot(null);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addCalculationInformationToBody(DtoAbstractAction dtoAction) {
        try{
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(ResourcesConstants.CALCULATION_FXML_INCLUDE_RESOURCE);
            loader.setLocation(url);
            //loader.setRoot(new VBox());
            Node tile = loader.load();
            CalculateComponentController tileController = loader.getController();

            // set information
            tileController.setTxtTypeLabel(dtoAction.getType());
            tileController.setTxtPrimaryLabel(dtoAction.getPrimaryEntity());
            tileController.setTxtSecondaryExistLabel(dtoAction.isSecondaryExist() == true ? "Yes" : "No");
            tileController.setTxtSecondaryLabel(dtoAction.getSecondaryEntity());
            tileController.setTxtOperatorLabel(((DtoCalculation)dtoAction).getOperatorType());
            tileController.setTxtFirstArgumentLabel(((DtoCalculation)dtoAction).getArg1());
            tileController.setTxtSecondArgumentLabel(((DtoCalculation)dtoAction).getArg2());
            tileController.setTxtResultPropertyLabel(((DtoCalculation)dtoAction).getResultProp());

            informationDetailsBody.getChildren().add(tile);
           // loader.setRoot(null);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addDecreaseInformationToBody(DtoAbstractAction dtoAction) {
        try{
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(ResourcesConstants.DECREASE_FXML_INCLUDE_RESOURCE);
            loader.setLocation(url);
            //loader.setRoot(new VBox());
            Node tile = loader.load();
            //Parent tile = loader.load();
            DecreaseComponentController tileController = loader.getController();

            // set information
            tileController.setTxtTypeLabel(dtoAction.getType());
            tileController.setTxtPrimaryLabel(dtoAction.getPrimaryEntity());
            tileController.setTxtSecondaryExistLabel(dtoAction.isSecondaryExist() == true ? "Yes" : "No");
            tileController.setTxtSecondaryLabel(dtoAction.getSecondaryEntity());
            tileController.setTxtPropertyLabel(((DtoDecrease)dtoAction).getProperty());
            tileController.setTxtByLabel(((DtoDecrease)dtoAction).getByExpression());

            informationDetailsBody.getChildren().add(tile);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addIncreaseInformationToBody(DtoAbstractAction dtoAction) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(ResourcesConstants.INCREACE_FXML_INCLUDE_RESOURCE);
            loader.setLocation(url);
            //loader.setRoot(new VBox());
            Node tile = loader.load();
            //Parent tile = loader.load();
            IncreaseComponentController tileController = loader.getController();

            // set information
            tileController.setTxtTypeLabel(dtoAction.getType());
            tileController.setTxtPrimaryLabel(dtoAction.getPrimaryEntity());
            tileController.setTxtSecondaryExistLabel(dtoAction.isSecondaryExist() == true ? "Yes" : "No");
            tileController.setTxtSecondaryLabel(dtoAction.getSecondaryEntity());
            tileController.setTxtPropertyLabel(((DtoIncrease)dtoAction).getProperty());
            tileController.setTxtByLabel(((DtoIncrease)dtoAction).getByExpression());

            informationDetailsBody.getChildren().add(tile);
           // loader.setRoot(null);

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

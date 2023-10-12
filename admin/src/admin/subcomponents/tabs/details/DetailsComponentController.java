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
import admin.subcomponents.app.StopTaskObject;
import admin.subcomponents.app.task.TaskThreadPoolUpdater;
import admin.subcomponents.common.ResourcesConstants;
import admin.util.http.HttpAdminUtil;
import dto.Dto;
import dto.entity.DtoEntity;
import dto.env.DtoEnv;
import dto.property.DtoProperty;
import dto.rule.Action.*;
import dto.rule.DtoRule;
import dto.termination.DtoTermination;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import admin.subcomponents.app.AppController;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

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
    private final static String GRID_TITLE = "Grid";
    private final static String ROOT_TITEL = "Loded XMLS in the system";
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

    // FXML iformation for thread pool manager
    private StopTaskObject stopThreadPool;

    @FXML private Label waitingThreadPoolLabel;
    private SimpleLongProperty propertyWaitingThreadPool;
    @FXML private Label runningThreadPoolLabel;
    private SimpleLongProperty propertyRunningThreadPool;
    @FXML private Label completedThreadPoolLabel;
    private SimpleLongProperty propertyCompletedThreadPool;
    @FXML private Label maxThreadsNumberThreadPoolLabel;
    private SimpleLongProperty propertyMaxThreadsNumberThreadPool;
    @FXML
    private TextField threadNewNumber;
    @FXML
    private Button updateThreadPoolNumber;
    @FXML
    private GridPane gridPanelThreadsPool;

    //ctor
    public DetailsComponentController() {
        stopThreadPool = new StopTaskObject();
        propertyWaitingThreadPool = new SimpleLongProperty();
        propertyRunningThreadPool = new SimpleLongProperty();
        propertyCompletedThreadPool = new SimpleLongProperty();
        propertyMaxThreadsNumberThreadPool = new SimpleLongProperty();
    }

    @FXML
    public void initialize() {
        waitingThreadPoolLabel.textProperty().bind(Bindings.format("%,d", propertyWaitingThreadPool));
        runningThreadPoolLabel.textProperty().bind(Bindings.format("%,d", propertyRunningThreadPool));
        completedThreadPoolLabel.textProperty().bind(Bindings.format("%,d", propertyCompletedThreadPool));
        maxThreadsNumberThreadPoolLabel.textProperty().bind(Bindings.format("%,d",propertyMaxThreadsNumberThreadPool));
        // start updating thread pool information
        resetThreadPoolLabelsInformation();
        resetThreadPoolMaxNumDefault();
        runTaskThreadPool();
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void loadDetailsView(){
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
        TreeItem<String> rootItem = new TreeItem<>(ROOT_TITEL);

        // create tree Item for every xml dto
        for (Dto currDto : mainController.getModel().getDtoXmlManager().getDtoXmlManager().values()){
           // create title by XML name
            TreeItem<String> xmlNameItem = new TreeItem<>("XML : " + currDto.getXmlName());

            Collection<DtoEntity> dtoEntityLst = currDto.getEntities().values();
            Collection<DtoEnv> dtoEnvLst = currDto.getEnvs().values();
            Collection<DtoRule> dtoRuleLst = currDto.getRules().values();

            // create view of entities by entities names
            if(dtoEntityLst!= null && !dtoEntityLst.isEmpty()) {
                TreeItem<String> branchItemEntities = new TreeItem<>(ENTITIES_TITLE);
                // add leafs by entities names
                for (DtoEntity curr : dtoEntityLst){
                    branchItemEntities.getChildren().add(new TreeItem<>(curr.getEntityName()));
                }
                xmlNameItem.getChildren().add(branchItemEntities);
            }

            // create view of env values by names
            if(dtoEnvLst != null && !dtoEnvLst.isEmpty()){
                TreeItem<String> branchItemEnvs = new TreeItem<>(ENVS_TITLE);
                // add leafs by env vars names
                for(DtoEnv curr : dtoEnvLst){
                    branchItemEnvs.getChildren().add(new TreeItem<>(curr.getEnvName()));
                }
                xmlNameItem.getChildren().add(branchItemEnvs);
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
                xmlNameItem.getChildren().add(branchItemRules);
            }

            // create view of Grid
            TreeItem<String> branchItemGrid = new TreeItem<>(GRID_TITLE);
            xmlNameItem.getChildren().add(branchItemGrid);

            // set curr xml into the root's children
            rootItem.getChildren().add(xmlNameItem);
        }
        // after all xmls added to Root, set tree view
        treeViewInformation.setRoot(rootItem);
    }

    @FXML
    void selectItem(MouseEvent event) {
        String txt = "";
        Dto dto;
        TreeItem<String> item = treeViewInformation.getSelectionModel().getSelectedItem();

        if(item != null && !item.getValue().equals(ROOT_TITEL)){
            String xmlName = getXmlNameForSelectedItem(item);
            dto = mainController.getModel().getDtoByXmlName(xmlName);

            boolean isValueRuleAction = false;
            int indexChildInRuleLst;
            DtoRule dtoRule;

            // reset info in screen
            informationDetailsTitle.setText("");
            // clear details body
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

    private String getXmlNameForSelectedItem(TreeItem<String> item) {
        String helperString = "";
        String xmlName = "";

        // can be up to 5 level deep (for clicking on action type)-
        // take the xml value one level before the root title
        if(item.getParent().getValue().equals(ROOT_TITEL)){
            helperString = item.getValue();
        }
        else if (item.getParent().getParent().getValue().equals(ROOT_TITEL)){
            helperString = item.getParent().getValue();
        }
        else if(item.getParent().getParent().getParent().getValue().equals(ROOT_TITEL)){
            helperString = item.getParent().getParent().getValue();
        }
        else if(item.getParent().getParent().getParent().getParent().getValue().equals(ROOT_TITEL)){
            helperString = item.getParent().getParent().getParent().getValue();
        }
        else if(item.getParent().getParent().getParent().getParent().getParent().getValue().equals(ROOT_TITEL)){
            helperString = item.getParent().getParent().getParent().getParent().getValue();
        }

        if(!helperString.equals("")) {
            xmlName = helperString.substring(6);
        }
        return xmlName;
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
            //tileController.setTxtTargetEntityLabel(((DtoProximity)dtoAction).getTargetEntity());
            tileController.setTxtTargetEntityLabel(dtoAction.getTargetEntity());
            //tileController.setTxtDepthLabel(((DtoProximity)dtoAction).getEnvDepthOf());
            tileController.setTxtDepthLabel(dtoAction.getEnvDepthOf());

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
            //tileController.setTxtCreateEntityLable(((DtoReplace)dtoAction).getCreateEntity());
            tileController.setTxtCreateEntityLable(dtoAction.getCreateEntity());
            //tileController.setTxtModeLable(((DtoReplace)dtoAction).getMode());
            tileController.setTxtModeLable(dtoAction.getMode());

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
            //tileController.setTxtPropertyLabel(((DtoSet)dtoAction).getProperty());
            tileController.setTxtPropertyLabel(dtoAction.getProperty());
            //tileController.setTxtValueLable(((DtoSet)dtoAction).getNewValue());
            tileController.setTxtValueLable(dtoAction.getNewValue());

            informationDetailsBody.getChildren().add(tile);
           // loader.setRoot(null);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addConditionInformationToBody(DtoAbstractAction dtoAction) {
        String singularity = dtoAction.getSingularity();
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
            Node tile = loader.load();

            if(singularity.equals("single")){
                SingleConditionComponentController tileController = loader.getController();
                // set information
                tileController.setTxtTypeLabel("Single Condition");
                tileController.setTxtPrimaryLabel(dtoAction.getPrimaryEntity());
                tileController.setTxtSecondaryExistLabel(dtoAction.isSecondaryExist() == true ? "Yes" : "No");
                tileController.setTxtSecondaryLabel(dtoAction.getSecondaryEntity());
                tileController.setTxtPropertyLabel(dtoAction.getDtoSingleCondition().getProperty());
                tileController.setTxtOperatorLabel(dtoAction.getDtoSingleCondition().getOperator());
                tileController.setTxtValueLabel(dtoAction.getDtoSingleCondition().getValue());
                tileController.setTxtThenConditionLabel(((Integer)dtoAction.getThenConditionsNumber()).toString());
                tileController.setTxtElseConditionLabel(((Integer)dtoAction.getElseConditionsNumber()).toString());

               /* tileController.setTxtPropertyLabel((( (DtoCondition) dtoAction).getDtoSingleCondition()).getProperty());
                tileController.setTxtOperatorLabel((( (DtoCondition) dtoAction).getDtoSingleCondition()).getOperator());
                tileController.setTxtValueLabel((( (DtoCondition) dtoAction).getDtoSingleCondition()).getValue());*/
               /* Integer thenConition = ((DtoCondition)dtoAction).getThenConditionsNumber();
                Integer elseCondition = ((DtoCondition)dtoAction).getElseConditionsNumber();
                tileController.setTxtThenConditionLabel(thenConition.toString());
                tileController.setTxtThenConditionLabel(elseCondition.toString());*/
            }
            else{
                MultipleConditionComponentController tileController = loader.getController();
                // set information
                tileController.setTxtTypeLabel("Multiple Condition");
                tileController.setTxtPrimaryLabel(dtoAction.getPrimaryEntity());
                tileController.setTxtSecondaryExistLabel(dtoAction.isSecondaryExist() == true ? "Yes" : "No");
                tileController.setTxtSecondaryLabel(dtoAction.getSecondaryEntity());
                tileController.setTxtLogicalLabel(dtoAction.getDtoMultipleCondition().getLogic());
                tileController.setTxtConditionNumberLabel(((Integer)dtoAction.getDtoMultipleCondition().getConditionNumber()).toString());
                tileController.setTxtThenConditionLabel(((Integer)dtoAction.getThenConditionsNumber()).toString());
                tileController.setTxtElseConditionLabel(((Integer)dtoAction.getElseConditionsNumber()).toString());

               /* tileController.setTxtLogicalLabel((( (DtoCondition) dtoAction).getDtoMultipleCondition()).getLogic());
                Integer number = ((( (DtoCondition) dtoAction).getDtoMultipleCondition()).getConditionNumber());
                tileController.setTxtConditionNumberLabel(number.toString());
                Integer thenConition = ((DtoCondition)dtoAction).getThenConditionsNumber();
                Integer elseCondition = ((DtoCondition)dtoAction).getElseConditionsNumber();
                tileController.setTxtThenConditionLabel(thenConition.toString());
                tileController.setTxtThenConditionLabel(elseCondition.toString());*/
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
            tileController.setTxtOperatorLabel(dtoAction.getOperatorType());
            tileController.setTxtFirstArgumentLabel(dtoAction.getArg1());
            tileController.setTxtFirstArgumentLabel(dtoAction.getArg2());
            tileController.setTxtResultPropertyLabel(dtoAction.getResultProp());

            /*tileController.setTxtOperatorLabel(((DtoCalculation)dtoAction).getOperatorType());
            tileController.setTxtFirstArgumentLabel(((DtoCalculation)dtoAction).getArg1());
            tileController.setTxtSecondArgumentLabel(((DtoCalculation)dtoAction).getArg2());
            tileController.setTxtResultPropertyLabel(((DtoCalculation)dtoAction).getResultProp());*/

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
            tileController.setTxtPropertyLabel(dtoAction.getProperty());
            tileController.setTxtByLabel(dtoAction.getByExpression());

           /* tileController.setTxtPropertyLabel(((DtoDecrease)dtoAction).getProperty());
            tileController.setTxtByLabel(((DtoDecrease)dtoAction).getByExpression());*/

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
            //tileController.setTxtPropertyLabel(((DtoIncrease)dtoAction).getProperty());
            tileController.setTxtPropertyLabel(dtoAction.getProperty());
            //tileController.setTxtByLabel(((DtoIncrease)dtoAction).getByExpression());
            tileController.setTxtByLabel(dtoAction.getByExpression());

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


    // Thread pool functions

    public StopTaskObject getStopThreadPool() {
        return stopThreadPool;
    }
    public void setStopThreadPool(boolean bool) {
        this.stopThreadPool.setStop(bool);
    }

    @FXML
    void updateThreadPoolNumberAction(ActionEvent event) {
        // reset value in threadpool - if boolean is true thread pool has already ran
        if(stopThreadPool.isStop()){
            stopThreadPool.setStop(false);
            resetThreadPoolLabelsInformation();
        }

        try {
            // validate input is a number
            int newThreadNumber = Integer.parseInt(threadNewNumber.getText());

            // create new HTTP request to set new thread number
            Response response = updateThreadPoolNumberRequest(threadNewNumber.getText());

            if(response.isSuccessful()){
                System.out.println("Response updateThreadPoolNumber is valid - Thread max number was updated to :" + newThreadNumber);
                // set property number
                propertyMaxThreadsNumberThreadPool.setValue(newThreadNumber);
                threadNewNumber.setText("");
                runTaskThreadPool();
            }
            else{
                System.out.println("Response updateThreadPoolNumber is NOT valid");
                mainController.showPopup("Error: update thread pool number failed, " + response.message());
            }
            response.close();
        }
        catch (NumberFormatException e){
            mainController.showPopup("Error: Input is not a number, please try again");
        }
        catch (IOException e){
            mainController.showPopup(e.getMessage());
        }
    }

    private Response updateThreadPoolNumberRequest(String newThreadNumber) throws IOException {
        String finalUrl = HttpUrl
                .parse(ResourcesConstants.UPDATE_THREAD_POOL_NUMBER)
                .newBuilder()
                .addQueryParameter("newNumber", newThreadNumber)
                .build()
                .toString();

        System.out.println("New request is launched for: " + finalUrl);
        Request request = new Request.Builder()
                .url(finalUrl).build();
        Call call = HttpAdminUtil.HTTP_CLIENT.newCall(request);
        return call.execute();
    }

    public void resetThreadPoolLabelsInformation(){
        propertyWaitingThreadPool.set(0);
        propertyRunningThreadPool.set(0);
        propertyCompletedThreadPool.set(0);
    }
    public void resetThreadPoolMaxNumDefault(){
        propertyMaxThreadsNumberThreadPool.set(1);
    }

    public void runTaskThreadPool(){
        // create task to update the thread pool information
        stopThreadPool.setStop(true);
        new Thread(new TaskThreadPoolUpdater(propertyWaitingThreadPool, propertyRunningThreadPool, propertyCompletedThreadPool, stopThreadPool)).start();
    }

}

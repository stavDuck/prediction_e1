package engine.validation.actionsValidator;

import engine.action.FunctionHelper;
import engine.execution.context.Context;
import engine.validation.ValidationCommonFunctions;
import generated.*;

import java.util.List;
import java.util.Random;

public class ActionValidator {
    public boolean validateActionData(PRDAction prdAction, PRDEntities entities, PRDEvironment prdEvironment) {
        boolean res = false;
        PRDEntity actionEntity = null;
        List<PRDEntity> prdEntityList = entities.getPRDEntity();
        int byValue;
        //get the entity from action
        for(PRDEntity prdEntity : prdEntityList) {
            if(prdEntity.getName().equals(prdAction.getEntity())) {
                actionEntity = prdEntity;
                res = true;
                break;
            }
        }
        //get the property from action
        if(!(prdAction.getType().equals("kill")) && actionEntity != null) {
            List<PRDProperty> prdPropertyList = actionEntity.getPRDProperties().getPRDProperty();
            for(PRDProperty prdProperty : prdPropertyList) {
                if(prdProperty.getPRDName().equals(prdAction.getProperty())) {
                    break;
                }
            }

        }
        if(prdAction.getType().equals("increase") || prdAction.getType().equals("decrease") ||prdAction.getType().equals("calculation")) {
            res = VerifyTypeIsNumber(prdAction.getBy(), prdEvironment);
        }
        return res;
    }

    public static boolean VerifyTypeIsNumber(String expression, PRDEvironment prdEvironment) {
        String value;
        if (expression.contains("environment(")) {
            value = environmentType(expression.substring(
                    expression.indexOf("environment(") + 12,
                    expression.indexOf(")")), prdEvironment);
            if (value.equals("decimal") || value.equals("float")) {
                return true;
            }
        } else if (expression.contains("random(")) {
            String randNum = expression.substring(
                    expression.indexOf("random(") + 7,
                    expression.indexOf(")"));
            if (randNum.matches("\\d+")) {
                return true;
            }
        }
        // assuming suitable int/ float
        else {
            if (expression.matches("\\d+")) {
                return true;
            } else if (expression.matches("\\d+\\.\\d+")) {
                return true;
            }
        }
        return false;
    }

    public static String environmentType(String nameProp, PRDEvironment prdEvironment) {
        List<PRDEnvProperty> prdEnvProperties = prdEvironment.getPRDEnvProperty();
        for(PRDEnvProperty property : prdEnvProperties) {
            if(property.getPRDName().equals(nameProp)) {
                return property.getType();
            }
        }
        return null;
    }

}

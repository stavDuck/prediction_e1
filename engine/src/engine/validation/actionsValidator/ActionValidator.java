package engine.validation.actionsValidator;

import engine.validation.exceptions.XmlValidationException;
import generated.*;
import java.util.List;

public class ActionValidator {
    public void validateActionData(PRDAction prdAction, PRDEntities entities, PRDEnvironment prdEvironment) throws XmlValidationException {
        PRDEntity actionEntity = null;
        boolean isPropertyFound = false;
        if(prdAction.getType().equals("replace")) {
            actionEntity = checkEntityExist(prdAction.getKill(), entities);
        }
        else if(prdAction.getType().equals("proximity")) {
            actionEntity = checkEntityExist(prdAction.getPRDBetween().getSourceEntity(), entities);
        }
        else {
            actionEntity = checkEntityExist(prdAction.getEntity(), entities);
        }
        //check property exists, in case action isn't kill
        if((prdAction.getType().equals("increase") || prdAction.getType().equals("decrease") || prdAction.getType().equals("set")) && actionEntity != null) {
            List<PRDProperty> prdPropertyList = actionEntity.getPRDProperties().getPRDProperty();
            for (PRDProperty prdProperty : prdPropertyList) {
                if (prdProperty.getPRDName().equals(prdAction.getProperty())) {
                    isPropertyFound = true;
                    break;
                }
            }
            if (!isPropertyFound) {
                throw new XmlValidationException("Action property: " + prdAction.getProperty() + " doesn't exist");
            }
        }
        // validation conditions when/ then/ else
        validateActionByType(prdAction, prdEvironment, entities, actionEntity);
    }

    public PRDEntity checkEntityExist(String entityFromAction, PRDEntities entities) throws XmlValidationException {
        PRDEntity actionEntity = null;
        List<PRDEntity> prdEntityList = entities.getPRDEntity();
        //check entity exists
        for(PRDEntity prdEntity : prdEntityList) {
            if(prdEntity.getName().equals(entityFromAction)) {
                actionEntity = prdEntity;
                break;
            }
        }

        // if actionEntity doesn't exist
        if(actionEntity == null){
            throw new XmlValidationException("Action entity: " + entityFromAction + " doesn't exist");
        }
        return actionEntity;
    }
    public void verifyTypeIsNumber(String expression, PRDEnvironment prdEvironment, PRDEntity prdEntity) throws XmlValidationException {
        String value;
        PRDProperty property = checkProperty(expression, prdEntity);
        //if environment function helper
        if (expression.contains("environment(")) {
            value = environmentType(expression.substring(
                    expression.indexOf("environment(") + 12,
                    expression.indexOf(")")), prdEvironment);
            if (!value.equals("decimal") && !value.equals("float")) {
                throw new XmlValidationException("Environment property type is: " + value +
                        ", which is not numeric");
            }
        }
        //if random function helper
        else if (expression.contains("random(")) {
            String randNum = expression.substring(
                    expression.indexOf("random(") + 7,
                    expression.indexOf(")"));
            if (!randNum.matches("\\d+")) {
                throw new XmlValidationException("Random value is: " + randNum +
                        ", which is not numeric");
            }
        }
        else if(expression.contains("evaluate(")) {

        }
        else if(expression.contains("percent(") || expression.contains("ticks(")) {

        }
        //if environment property
        else if (property != null) {
            if (!((property.getType()).equals("decimal")) && !((property.getType()).equals("float"))) {
                throw new XmlValidationException("Property type is: " + property +
                        ", which is not numeric");
            }
        }
        // if free value
        else {
            if (!(expression.matches("\\d+")) && !(expression.matches("\\d+\\.\\d+"))) {
                throw new XmlValidationException("Expression value is: " + expression +
                        ", which is not numeric");
            }
        }
    }

    public String environmentType(String nameProp, PRDEnvironment prdEvironment) {
        List<PRDEnvProperty> prdEnvProperties = prdEvironment.getPRDEnvProperty();
        for(PRDEnvProperty property : prdEnvProperties) {
            if(property.getPRDName().equals(nameProp)) {
                return property.getType();
            }
        }
        return null;
    }

    public PRDProperty checkProperty(String expression, PRDEntity prdEntity) {
        for(PRDProperty prdProperty : prdEntity.getPRDProperties().getPRDProperty()) {
            if (prdProperty.getPRDName().equals(expression)) {
                return prdProperty;
            }
        }
        return null;
    }

    public void validateCalculationAction(PRDAction prdAction, PRDEnvironment prdEvironment, PRDEntity actionEntity) throws XmlValidationException {
        //check result-prop is a number
        verifyTypeIsNumber(prdAction.getResultProp(), prdEvironment, actionEntity);
        if(prdAction.getPRDMultiply() != null) {
            //verify both args
            verifyTypeIsNumber(prdAction.getPRDMultiply().getArg1(), prdEvironment, actionEntity);
            verifyTypeIsNumber(prdAction.getPRDMultiply().getArg2(), prdEvironment, actionEntity);
        }
        else if(prdAction.getPRDDivide() != null) {
            //verify both args
            verifyTypeIsNumber(prdAction.getPRDDivide().getArg1(), prdEvironment, actionEntity);
            verifyTypeIsNumber(prdAction.getPRDDivide().getArg2(), prdEvironment, actionEntity);
        }
        else {
            throw new XmlValidationException("Invalid calculation action. Action can only be of type divide and multiply.");
        }
    }

    public void validateCondition(PRDAction prdAction, PRDEvironment prdEvironment, PRDEntities entities, PRDEntity actionEntity) throws XmlValidationException {
        validateWhenCondition(prdAction.getPRDCondition(), prdEvironment, actionEntity);
        validateThenCondition(prdAction.getPRDThen(), prdEvironment, entities, actionEntity);
        if(prdAction.getPRDElse() != null) {
            validateElseCondition(prdAction.getPRDElse(), prdEvironment, entities, actionEntity);
        }
    }

    public void validateWhenCondition(PRDCondition prdCondition, PRDEvironment prdEvironment, PRDEntity actionEntity) throws XmlValidationException {
        if (prdCondition.getSingularity().equals("multiple")) {
            for (PRDCondition condition : prdCondition.getPRDCondition()) {
                if (condition.getSingularity().equals("multiple")) {
                    validateWhenCondition(condition, prdEvironment, actionEntity);
                } else {
                    validateSingleCondition(condition, prdEvironment, actionEntity);
                }
            }
        } else {
            validateSingleCondition(prdCondition, prdEvironment, actionEntity);
        }
    }

    public void validateThenCondition(PRDThen prdThen, PRDEvironment prdEvironment, PRDEntities entities, PRDEntity actionEntity) throws XmlValidationException {
        for (PRDAction action : prdThen.getPRDAction()) {
            validateActionByType(action, prdEvironment, entities, actionEntity);
        }
    }

    public void validateElseCondition(PRDElse prdElse, PRDEvironment prdEvironment, PRDEntities entities, PRDEntity actionEntity) throws XmlValidationException {
        for (PRDAction action : prdElse.getPRDAction()) {
            validateActionByType(action, prdEvironment, entities, actionEntity);
        }
    }

    public void validateTypeIsBoolean(String expression, PRDEvironment prdEvironment, PRDEntity prdEntity) throws XmlValidationException {
        String value;
        PRDProperty property = checkProperty(expression, prdEntity);
        //if environment function helper
        if (expression.contains("environment(")) {
            value = environmentType(expression.substring(
                    expression.indexOf("environment(") + 12,
                    expression.indexOf(")")), prdEvironment);
            if (!value.equals("boolean") && !value.equals("string")) {
                throw new XmlValidationException("Environment property type is: " + value +
                        ", which is not boolean");
            }
        }
        //if random function helper
        else if (expression.contains("random(")) {
            throw new XmlValidationException("Expression is of type boolean and can't use the random function helper");
        }
        //if environment property
        else if (property != null) {
            if (!((property.getType()).equals("boolean")) && !((property.getType()).equals("string"))) {
                throw new XmlValidationException("Property type is: " + property +
                        ", which is not boolean");
            }
        }
        // if free value
        else {
            if (!(expression.equals("true")) && !(expression.equals("false"))) {
                throw new XmlValidationException("Expression value is: " + expression +
                        ", which is not boolean");
            }
        }
    }

    public void validateTypeIsString(String expression, PRDEvironment prdEvironment, PRDEntity prdEntity) throws XmlValidationException {
        String value;
        PRDProperty property = checkProperty(expression, prdEntity);
        //if environment function helper
        if (expression.contains("environment(")) {
            value = environmentType(expression.substring(
                    expression.indexOf("environment(") + 12,
                    expression.indexOf(")")), prdEvironment);
            if (!value.equals("string")) {
                throw new XmlValidationException("Environment property type is: " + value +
                        ", which is not string");
            }
        }
        //if random function helper
        else if (expression.contains("random(")) {
            throw new XmlValidationException("Expression is of type string and can't use the random function helper");
        }
        //if environment property
        else if (property != null) {
            if (!((property.getType()).equals("string"))) {
                throw new XmlValidationException("Property type is: " + property +
                        ", which is not string");
            }
        }
        // free value is a regular string, no need to verfy
    }

    public void validateSet(PRDAction prdAction, PRDEvironment prdEvironment, PRDEntity prdEntity) throws XmlValidationException {
        PRDProperty property = checkProperty(prdAction.getProperty(), prdEntity);
        if (property == null) {
            throw new XmlValidationException("Property: " + prdAction.getProperty() + " is invalid");
        } else {
            if (property.getType().equals("decimal") || property.getType().equals("float")) {
                verifyTypeIsNumber(prdAction.getValue(), prdEvironment, prdEntity);
            } else if (property.getType().equals("boolean")) {
                validateTypeIsBoolean(prdAction.getValue(), prdEvironment, prdEntity);
            } else if (property.getType().equals("string")) {
                validateTypeIsString(prdAction.getValue(), prdEvironment, prdEntity);
            } else {
                throw new XmlValidationException("Property type: "+ property.getType() + " is invalid");
            }
        }
    }

    public void validateReplace(PRDAction prdAction, PRDEvironment prdEvironment, PRDEntities entities, PRDEntity actionEntity) throws XmlValidationException {
        //need to only check the 'create' entity exist
        checkEntityExist(prdAction.getCreate(), entities);
    }

    public void validateProximity(PRDAction prdAction, PRDEvironment prdEvironment, PRDEntities entities, PRDEntity actionEntity) throws XmlValidationException {
        //need to only check the 'target' entity exist
        checkEntityExist(prdAction.getPRDBetween().getTargetEntity(), entities);

        //need to check 'of' expression is a number
    }
    public void validateActionByType(PRDAction prdAction, PRDEvironment prdEvironment, PRDEntities entities, PRDEntity actionEntity) throws XmlValidationException {
       // check if entity exist - WHY????????
        /*if(!(prdAction.getEntity().equals(actionEntity.getName()))) {
            throw new XmlValidationException("Action: " + prdAction.getType() + " entity: " + prdAction.getEntity() + " is not found, please check the entity name is correct.");
        }*/
        if(prdAction.getType().equals("increase") || prdAction.getType().equals("decrease")) {
            // check if property exist
            if(!isPropertyExistInAction(actionEntity.getPRDProperties().getPRDProperty(), prdAction.getProperty())){
                throw new XmlValidationException("Action: " + prdAction.getType() + " entity: " + prdAction.getEntity() + " property: " + prdAction.getProperty() +
                        " is not found, please check the property name is correct.");
            }
            verifyTypeIsNumber(prdAction.getBy(), prdEvironment, actionEntity);
        }

        else if(prdAction.getType().equals("calculation")) {
            if(!isPropertyExistInAction(actionEntity.getPRDProperties().getPRDProperty(), prdAction.getResultProp())){
                throw new XmlValidationException("Action: " + prdAction.getType() + " entity: " + prdAction.getResultProp() + " property: " + prdAction.getProperty() +
                        " is not found, please check the property name is correct.");
            }
            validateCalculationAction(prdAction, prdEvironment, actionEntity);
        }

        else if(prdAction.getType().equals("condition")) {
            validateCondition(prdAction, prdEvironment, entities, actionEntity);
        }
        else if(prdAction.getType().equals("set")) {
            if(!isPropertyExistInAction(actionEntity.getPRDProperties().getPRDProperty(), prdAction.getProperty())){
                throw new XmlValidationException("Action: " + prdAction.getType() + " entity: " + prdAction.getEntity() + " property: " + prdAction.getProperty() +
                        " is not found, please check the property name is correct.");
            }
            validateSet(prdAction, prdEvironment, actionEntity);
        }
        else if(prdAction.getType().equals("replace")) {
            validateReplace(prdAction, prdEvironment, entities, actionEntity);
        }
        else if(prdAction.getType().equals("proximity")) {
            validateProximity(prdAction, prdEvironment, entities, actionEntity);
        }
    }

    private boolean isPropertyExistInAction(List<PRDProperty> lst, String propName){
        boolean isFound = false;
        for(PRDProperty curr : lst){
            if(curr.getPRDName().equals(propName)) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }

    public void validateSingleCondition(PRDCondition prdCondition, PRDEvironment prdEvironment, PRDEntity actionEntity) throws XmlValidationException {
        PRDProperty property = checkProperty(prdCondition.getProperty(), actionEntity);
        if (property == null) {
            throw new XmlValidationException("Property: " + prdCondition.getProperty() + " is invalid");
        } else {
            if (property.getType().equals("decimal") || property.getType().equals("float")) {
                verifyTypeIsNumber(prdCondition.getValue(), prdEvironment, actionEntity);
            } else if (property.getType().equals("boolean")) {
                if(prdCondition.getOperator().equals("bt") || prdCondition.getOperator().equals("lt"))
                    throw new XmlValidationException("Property " + property.getPRDName() + " is of type boolean. operator " + prdCondition.getOperator() + " can only be used with numeric type.");
                else
                    validateTypeIsBoolean(prdCondition.getValue(), prdEvironment, actionEntity);
            } else if (property.getType().equals("string")) {
                if(prdCondition.getOperator().equals("bt") || prdCondition.getOperator().equals("lt"))
                    throw new XmlValidationException("Property " + property.getPRDName() + " is of type String. operator " + prdCondition.getOperator() + " can only be used with numeric type.");
                else
                    validateTypeIsString(prdCondition.getValue(), prdEvironment, actionEntity);
            } else {
                throw new XmlValidationException("Property type: " + property.getType() + " is invalid");
            }
        }
    }
}

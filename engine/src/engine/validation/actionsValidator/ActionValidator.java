package engine.validation.actionsValidator;

import engine.action.FunctionHelper;
import engine.validation.exceptions.XmlValidationException;
import generated.*;
import java.util.List;

public class ActionValidator {
    public void validateActionData(PRDAction prdAction, PRDEntities entities, PRDEnvironment prdEvironment) throws XmlValidationException {
        PRDEntity actionEntity = null;
        boolean isPropertyFound = false;
        if (prdAction.getType().equals("replace")) {
            actionEntity = checkEntityExist(prdAction.getKill(), entities);
        } else if (prdAction.getType().equals("proximity")) {
            actionEntity = checkEntityExist(prdAction.getPRDBetween().getSourceEntity(), entities);
        } else {
            actionEntity = checkEntityExist(prdAction.getEntity(), entities);
        }
        //check property exists, in case action isn't kill
        if ((prdAction.getType().equals("increase") || prdAction.getType().equals("decrease") || prdAction.getType().equals("set")) && actionEntity != null) {
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
        validateActionByType(prdAction, prdEvironment, entities, actionEntity, prdAction.getPRDSecondaryEntity().getEntity());
    }

    public PRDEntity checkEntityExist(String entityFromAction, PRDEntities entities) throws XmlValidationException {
        PRDEntity actionEntity = null;
        List<PRDEntity> prdEntityList = entities.getPRDEntity();
        //check entity exists
        for (PRDEntity prdEntity : prdEntityList) {
            if (prdEntity.getName().equals(entityFromAction)) {
                actionEntity = prdEntity;
                break;
            }
        }

        // if actionEntity doesn't exist
        if (actionEntity == null) {
            throw new XmlValidationException("Action entity: " + entityFromAction + " doesn't exist");
        }
        return actionEntity;
    }

    public void verifyTypeIsNumber(String expression, PRDEnvironment prdEvironment, PRDEntity prdEntity, String secondaryEntityName, PRDEntities entities) throws XmlValidationException {
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
        } else if (expression.contains("evaluate(")) {
            value = (getEvaluateProperty(FunctionHelper.extraceStringFromEvaluateFunc(expression), prdEntity, secondaryEntityName, entities).getType());
            if (!value.equals("decimal") && !value.equals("float")) {
                String name = expression.substring(expression.indexOf("."), expression.indexOf(")"));
                throw new XmlValidationException("property: " + name + ", type is: " + value +
                        ", which is not numeric");
            }
        } else if (expression.contains("percent(") || expression.contains("ticks(")) {

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
        for (PRDEnvProperty property : prdEnvProperties) {
            if (property.getPRDName().equals(nameProp)) {
                return property.getType();
            }
        }
        return null;
    }

    public PRDProperty checkProperty(String expression, PRDEntity prdEntity) throws XmlValidationException {
        for (PRDProperty prdProperty : prdEntity.getPRDProperties().getPRDProperty()) {
            if (prdProperty.getPRDName().equals(expression)) {
                return prdProperty;
            }
        }
        throw new XmlValidationException("Property: " + expression + " in entity: " + prdEntity.getName() + " doesn't exist");
    }

    public void validateCalculationAction(PRDAction prdAction, PRDEnvironment prdEvironment, PRDEntity actionEntity, String secondEntityName, PRDEntities entities) throws XmlValidationException {
        //check result-prop is a number
        verifyTypeIsNumber(prdAction.getResultProp(), prdEvironment, actionEntity, secondEntityName, entities);
        if (prdAction.getPRDMultiply() != null) {
            //verify both args
            verifyTypeIsNumber(prdAction.getPRDMultiply().getArg1(), prdEvironment, actionEntity, secondEntityName, entities);
            verifyTypeIsNumber(prdAction.getPRDMultiply().getArg2(), prdEvironment, actionEntity, secondEntityName, entities);
        } else if (prdAction.getPRDDivide() != null) {
            //verify both args
            verifyTypeIsNumber(prdAction.getPRDDivide().getArg1(), prdEvironment, actionEntity, secondEntityName, entities);
            verifyTypeIsNumber(prdAction.getPRDDivide().getArg2(), prdEvironment, actionEntity, secondEntityName, entities);
        } else {
            throw new XmlValidationException("Invalid calculation action. Action can only be of type divide and multiply.");
        }
    }

    public void validateCondition(PRDAction prdAction, PRDEnvironment prdEvironment, PRDEntities entities, PRDEntity actionEntity, String secondEntityName) throws XmlValidationException {
        validateWhenCondition(prdAction.getPRDCondition(), prdEvironment, actionEntity, secondEntityName, entities);
        //need to verify the secondary is still available in the 'then' condition
        validateThenCondition(prdAction.getPRDThen(), prdEvironment, entities, actionEntity, secondEntityName);
        if (prdAction.getPRDElse() != null) {
            validateElseCondition(prdAction.getPRDElse(), prdEvironment, entities, actionEntity, secondEntityName);
        }
    }

    public void validateWhenCondition(PRDCondition prdCondition, PRDEnvironment prdEvironment, PRDEntity actionEntity, String secondaryName, PRDEntities entities) throws XmlValidationException {
        if (prdCondition.getSingularity().equals("multiple")) {
            for (PRDCondition condition : prdCondition.getPRDCondition()) {
                if (condition.getSingularity().equals("multiple")) {
                    validateWhenCondition(condition, prdEvironment, actionEntity, secondaryName, entities);
                } else {
                    validateSingleCondition(condition, prdEvironment, actionEntity, secondaryName, entities);
                }
            }
        } else {
            validateSingleCondition(prdCondition, prdEvironment, actionEntity, secondaryName, entities);
        }
    }

    public void validateThenCondition(PRDThen prdThen, PRDEnvironment prdEvironment, PRDEntities entities, PRDEntity actionEntity, String secondEntityName) throws XmlValidationException {
        for (PRDAction action : prdThen.getPRDAction()) {
            validateActionByType(action, prdEvironment, entities, actionEntity, secondEntityName);
        }
    }

    public void validateElseCondition(PRDElse prdElse, PRDEnvironment prdEvironment, PRDEntities entities, PRDEntity actionEntity, String secondEntityName) throws XmlValidationException {
        for (PRDAction action : prdElse.getPRDAction()) {
            validateActionByType(action, prdEvironment, entities, actionEntity, secondEntityName);
        }
    }

    public void validateTypeIsBoolean(String expression, PRDEnvironment prdEvironment, PRDEntity prdEntity, String secondaryEntityName, PRDEntities entities) throws XmlValidationException {
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
            throw new XmlValidationException("Expression is of type boolean and can't use the 'random' function helper");
        } else if (expression.contains("evaluate(")) {
            value = (getEvaluateProperty(FunctionHelper.extraceStringFromEvaluateFunc(expression), prdEntity, secondaryEntityName, entities).getType());
            if (!value.equals("boolean") && !value.equals("string")) {
                String name = expression.substring(expression.indexOf("."), expression.indexOf(")"));
                throw new XmlValidationException("property: " + name + ", type is: " + value +
                        ", which is not boolean");
            }
            } else if (expression.contains("percent(")) {
            throw new XmlValidationException("Expression is of type boolean and can't use the 'percent' function helper");

        } else if (expression.contains("ticks(")) {
            throw new XmlValidationException("Expression is of type boolean and can't use the 'ticks' function helper");

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

    public void validateTypeIsString(String expression, PRDEnvironment prdEvironment, PRDEntity prdEntity, String secondaryEntityName, PRDEntities entities) throws XmlValidationException {
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
            throw new XmlValidationException("Expression is of type string and can't use the 'random' function helper");
        } else if (expression.contains("evaluate(")) {
            value = (getEvaluateProperty(FunctionHelper.extraceStringFromEvaluateFunc(expression), prdEntity, secondaryEntityName, entities).getType());
            if (!value.equals("string")) {
                String name = expression.substring(expression.indexOf("."), expression.indexOf(")"));
                throw new XmlValidationException("property: " + name + ", type is: " + value +
                        ", which is not string");
            }

        } else if (expression.contains("percent(")) {
            throw new XmlValidationException("Expression is of type string and can't use the 'percent' function helper");

        } else if (expression.contains("ticks(")) {
            throw new XmlValidationException("Expression is of type string and can't use the 'ticks' function helper");

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

    public void validateSet(PRDAction prdAction, PRDEnvironment prdEvironment, PRDEntity prdEntity, String secondEntityName, PRDEntities entities) throws XmlValidationException {
        PRDProperty property = checkProperty(prdAction.getProperty(), prdEntity);
        if (property == null) {
            throw new XmlValidationException("Property: " + prdAction.getProperty() + " is invalid");
        } else {
            if (property.getType().equals("decimal") || property.getType().equals("float")) {
                verifyTypeIsNumber(prdAction.getValue(), prdEvironment, prdEntity, secondEntityName, entities);
            } else if (property.getType().equals("boolean")) {
                validateTypeIsBoolean(prdAction.getValue(), prdEvironment, prdEntity, secondEntityName, entities);
            } else if (property.getType().equals("string")) {
                validateTypeIsString(prdAction.getValue(), prdEvironment, prdEntity, secondEntityName, entities);
            } else {
                throw new XmlValidationException("Property type: " + property.getType() + " is invalid");
            }
        }
    }

    public void validateReplace(PRDAction prdAction, PRDEnvironment prdEvironment, PRDEntities entities, PRDEntity actionEntity) throws XmlValidationException {
        //need to only check the 'create' entity exist
        checkEntityExist(prdAction.getCreate(), entities);
    }

    public void validateProximity(PRDAction prdAction, PRDEnvironment prdEvironment, PRDEntities entities, PRDEntity actionEntity) throws XmlValidationException {
        //need to only check the 'target' entity exist
        checkEntityExist(prdAction.getPRDBetween().getTargetEntity(), entities);

        //need to check 'of' expression is a number
    }

    public void validateActionByType(PRDAction prdAction, PRDEnvironment prdEvironment, PRDEntities entities, PRDEntity actionEntity, String secondEntityName) throws XmlValidationException {
        // check if entity exist - WHY????????
        /*if(!(prdAction.getEntity().equals(actionEntity.getName()))) {
            throw new XmlValidationException("Action: " + prdAction.getType() + " entity: " + prdAction.getEntity() + " is not found, please check the entity name is correct.");
        }*/
        if (prdAction.getType().equals("increase") || prdAction.getType().equals("decrease")) {
            // check if property exist
            if (!isPropertyExistInAction(actionEntity.getPRDProperties().getPRDProperty(), prdAction.getProperty())) {
                throw new XmlValidationException("Action: " + prdAction.getType() + " entity: " + prdAction.getEntity() + " property: " + prdAction.getProperty() +
                        " is not found, please check the property name is correct.");
            }
            verifyTypeIsNumber(prdAction.getBy(), prdEvironment, actionEntity, secondEntityName, entities);
        } else if (prdAction.getType().equals("calculation")) {
            if (!isPropertyExistInAction(actionEntity.getPRDProperties().getPRDProperty(), prdAction.getResultProp())) {
                throw new XmlValidationException("Action: " + prdAction.getType() + " entity: " + prdAction.getResultProp() + " property: " + prdAction.getProperty() +
                        " is not found, please check the property name is correct.");
            }
            validateCalculationAction(prdAction, prdEvironment, actionEntity, secondEntityName, entities);
        } else if (prdAction.getType().equals("condition")) {
            validateCondition(prdAction, prdEvironment, entities, actionEntity, secondEntityName);
        } else if (prdAction.getType().equals("set")) {
            if (!isPropertyExistInAction(actionEntity.getPRDProperties().getPRDProperty(), prdAction.getProperty())) {
                throw new XmlValidationException("Action: " + prdAction.getType() + " entity: " + prdAction.getEntity() + " property: " + prdAction.getProperty() +
                        " is not found, please check the property name is correct.");
            }
            validateSet(prdAction, prdEvironment, actionEntity, secondEntityName, entities);
        } else if (prdAction.getType().equals("replace")) {
            validateReplace(prdAction, prdEvironment, entities, actionEntity);
        } else if (prdAction.getType().equals("proximity")) {
            validateProximity(prdAction, prdEvironment, entities, actionEntity);
        }
    }

    private boolean isPropertyExistInAction(List<PRDProperty> lst, String propName) {
        boolean isFound = false;
        for (PRDProperty curr : lst) {
            if (curr.getPRDName().equals(propName)) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }

    public void validateSingleCondition(PRDCondition prdCondition, PRDEnvironment prdEvironment, PRDEntity actionEntity, String secondaryName, PRDEntities entities) throws XmlValidationException {
        //PRDProperty property = checkProperty(prdCondition.getProperty(), actionEntity);
        String propertyType = checkConditionProperty(prdCondition.getProperty(), prdEvironment, actionEntity, secondaryName, entities);

        if (propertyType.equals("decimal") || propertyType.equals("float")) {
            verifyTypeIsNumber(prdCondition.getValue(), prdEvironment, actionEntity, secondaryName, entities);
        } else if (propertyType.equals("boolean")) {
            if (prdCondition.getOperator().equals("bt") || prdCondition.getOperator().equals("lt"))
                throw new XmlValidationException("Condition property is of type boolean. operator " + prdCondition.getOperator() + " can only be used with numeric type.");
            else
                validateTypeIsBoolean(prdCondition.getValue(), prdEvironment, actionEntity, secondaryName, entities);
        } else if (propertyType.equals("string")) {
            if (prdCondition.getOperator().equals("bt") || prdCondition.getOperator().equals("lt"))
                throw new XmlValidationException("Condition property is of type String. operator " + prdCondition.getOperator() + " can only be used with numeric type.");
            else
                validateTypeIsString(prdCondition.getValue(), prdEvironment, actionEntity, secondaryName, entities);
        } else {
            throw new XmlValidationException("Condition property: '" + prdCondition.getProperty() + "' is invalid");
        }

    }

    //actionEntity is the entity that's in the action description (entity=...),
    // and the secondary entity is in the condition rule: PRD-Secondary-entity
    public String checkConditionProperty(String conditionProperty, PRDEnvironment prdEvironment, PRDEntity actionEntity, String secondaryEntityName, PRDEntities entities) throws XmlValidationException{
        PRDProperty property = checkProperty(conditionProperty, actionEntity);
        if (conditionProperty.contains("environment(")) {
            return environmentType(conditionProperty.substring(
                    conditionProperty.indexOf("environment(") + 12,
                    conditionProperty.indexOf(")")), prdEvironment);
        }
        //if random function helper
        else if (conditionProperty.contains("random(")) {
            return "float";
        } else if (conditionProperty.contains("evaluate(")) {
            return (getEvaluateProperty(FunctionHelper.extraceStringFromEvaluateFunc(conditionProperty), actionEntity, secondaryEntityName, entities).getType());

        } else if (conditionProperty.contains("percent(")) {
            return "float";
        } else if (conditionProperty.contains("ticks(")) {
            return "float";
        } else if (property != null) {
            return property.getType();
        } else {
            return "string";
        }
    }

    public PRDProperty getEvaluateProperty(String expression, PRDEntity prdEntity, String secondaryEntity, PRDEntities entities) throws XmlValidationException {
        String[] evaluateExp = expression.split(".");
        PRDEntity entity;
        PRDProperty property = null;
        try {
            //if there's a secondary entity
            if (secondaryEntity != null && evaluateExp[0].equalsIgnoreCase(secondaryEntity)) {
                entity = checkEntityExist(secondaryEntity, entities);
            }
            //if there isn't a secondary, there will be primary
            else {
                entity = prdEntity;
            }
            property = checkProperty(evaluateExp[1], entity);
        } catch (XmlValidationException e) {
            throw new XmlValidationException(e.getMessage());
        }
        return property;
    }

}

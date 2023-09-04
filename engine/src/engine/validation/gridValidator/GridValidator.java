package engine.validation.gridValidator;

import engine.validation.exceptions.XmlValidationException;
import generated.PRDEntity;
import generated.PRDWorld;

public class GridValidator {
    public void validateGridData(PRDWorld prdWorld) throws XmlValidationException {
        PRDWorld.PRDGrid prdGrid = prdWorld.getPRDGrid();
        if(!(prdGrid.getRows() >= 10 && prdGrid.getRows() <= 100)) {
            throw new XmlValidationException("Grid rows value: " + prdGrid.getRows() + " is out of range. Value should be between 10-100");
        }
        if(!(prdGrid.getColumns() >= 10 && prdGrid.getColumns() <= 100)) {
            throw new XmlValidationException("Grid columns value: " + prdGrid.getColumns() + " is out of range. Value should be between 10-100");
        }

    }

}

package dto.rule.Action;

public class DtoCalculation extends DtoAbstractAction{
    private String resultProp;
    private String arg1;
    private String arg2;
    private String operatorType;

    public DtoCalculation(String operatorType, String resultProp, String arg1, String arg2,
                          String type, String primaryEntity, boolean isSecondaryExist, String secondaryEntity) {
        super(type, primaryEntity, isSecondaryExist, secondaryEntity);
        this.operatorType = operatorType;
        this.resultProp = resultProp;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String getResultProp() {
        return resultProp;
    }

    public void setResultProp(String resultProp) {
        this.resultProp = resultProp;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }
}

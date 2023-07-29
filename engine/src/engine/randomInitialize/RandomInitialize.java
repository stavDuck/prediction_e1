package engine.randomInitialize;

public class RandomInitialize {
    boolean isRandom = false;
    int initVal;

    // In case of isRandom = true, initVal will be generated from
    // Property class with the range values and use this func
    public RandomInitialize(boolean isRandom, int initVal) {
        this.isRandom = isRandom;
        this.initVal = initVal;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public void setRandom(boolean random) {
        isRandom = random;
    }

    public int getInitVal() {
        return initVal;
    }

    public void setInitVal(int initVal) {
        this.initVal = initVal;
    }
}

package engine.value.generator.random.bool;

import engine.value.generator.random.AbstractRandomValueGenerator;

public class RandomBooleanValueGenerator extends AbstractRandomValueGenerator<Boolean> {
    @Override
    public Boolean generateValue() {
        return random.nextBoolean();
    }
}

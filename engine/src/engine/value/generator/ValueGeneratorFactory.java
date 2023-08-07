package engine.value.generator;

import engine.value.generator.random.bool.RandomBooleanValueGenerator;
import engine.value.generator.random.numeric.RandomFloatGenerator;
import engine.value.generator.random.numeric.RandomIntegerGenerator;
import engine.value.generator.random.string.RandomStringValueGenerator;

public interface ValueGeneratorFactory {

    static ValueGenerator<Boolean> createRandomBoolean() {
        return new RandomBooleanValueGenerator();
    }
    static ValueGenerator<Float> createRandomFloat(Float from, Float to) { return new RandomFloatGenerator(from, to);}
    static ValueGenerator<Integer> createRandomInteger(Integer from, Integer to) { return new RandomIntegerGenerator(from, to);}
    static ValueGenerator<String> createRandomString() { return new RandomStringValueGenerator();}
}

package engine.value.generator.random.numeric;

import engine.value.generator.random.AbstractRandomValueGenerator;

public abstract class AbstractNumericRandomGenerator<T> extends AbstractRandomValueGenerator<T> {
    protected final T from;
    protected final T to;

    protected AbstractNumericRandomGenerator(T from, T to) {
        this.from = from;
        this.to = to;
    }
}

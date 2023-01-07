package org.emerald.restfileserver.mapper;

import java.util.function.Function;

public interface Mapper<From, To> extends Function<From, To> {
    To map(From from);

    @Override
    default To apply(From from) {
        return map(from);
    }
}

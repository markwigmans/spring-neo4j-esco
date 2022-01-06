package com.btb.sne.mapping;

import java.util.Optional;

public class MapperUtils {

    public static <T> Optional<T> wrap(T object) {
        return Optional.ofNullable(object);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> T unwrap(Optional<T> object) {
        return object.orElse(null);
    }
}

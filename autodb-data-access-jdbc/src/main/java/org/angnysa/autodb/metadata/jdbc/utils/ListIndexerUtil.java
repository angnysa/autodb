package org.angnysa.autodb.metadata.jdbc.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListIndexerUtil {
    public static <T> Map<String, T> index(final List<T> list, final Function<T, String> nameGetter) {
        return index(list.stream(), nameGetter);
    }

    public static <T> Map<String, T> index(final Stream<T> stream, final Function<T, String> nameGetter) {
        return stream
                .filter(e -> nameGetter.apply(e) != null)
                .collect(Collectors.toUnmodifiableMap(
                        e -> nameGetter.apply(e).toLowerCase(),
                        Function.identity()));
    }
}

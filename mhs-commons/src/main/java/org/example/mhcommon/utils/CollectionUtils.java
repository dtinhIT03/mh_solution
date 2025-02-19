package org.example.mhcommon.utils;


import org.example.mhcommon.core.json.JsonArray;
import org.jooq.JSON;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class CollectionUtils {
    private CollectionUtils() {
    }

    public static <T> int size(Collection<T> list) {
        if (list == null || list.isEmpty()) return 0;
        return list.size();
    }

    public static <T> boolean empty(Collection<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> List<T> mergeList(Collection<T>... list) {
        return Arrays.stream(list)
                .flatMap(Collection::parallelStream)
                .collect(Collectors.toList());
    }

    public static <K, V> List<K> extractField(Collection<V> list, Function<V, K> fieldFunction) {
        return list.stream()
                .map(fieldFunction)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public static <K, V> List<K> extractField(Collection<V> list, Function<V, K> fieldFunction, Predicate<V> predicate) {
        return list.stream()
                .filter(predicate)
                .map(fieldFunction)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public static <K, V> Set<K> extractFieldToSet(Collection<V> list, Function<V, K> fieldFunction) {
        return list.stream()
                .map(fieldFunction)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public static <K, V> Set<K> extractFieldToSet(Collection<V> list, Function<V, K> fieldFunction, Predicate<V> predicate) {
        return list.stream()
                .filter(predicate)
                .map(fieldFunction)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public static <V> List<V> filterList(Collection<V> list, Predicate<V> predicate) {
        return list.stream()
                .filter(predicate)
                .distinct()
                .collect(Collectors.toList());
    }

    public static <K, V> Map<K, Long> groupCount(Collection<V> list, Predicate<V> predicate,
                                                 Function<V, K> keyFunction) {
        return list.stream()
                .filter(predicate)
                .collect(groupingBy(keyFunction, counting()));
    }

    public static <K, V> Map<K, V> filterCollectToMap(Collection<V> list, Predicate<V> predicate, Function<V, K> keyFunction) {
        return list.stream()
                .filter(predicate)
                .collect(toMap(keyFunction, Function.identity(), (v1, v2) -> v1));
    }

    public static <K, V> Map<K, V> collectToMap(Collection<V> list, Function<V, K> keyFunction) {
        return list.stream().collect(toMap(keyFunction, Function.identity(), (v1, v2) -> v1));
    }

    public static <K, V> Set<K> collectToSet(Collection<V> list, Function<V, K> keyFunction) {
        return list.stream()
                .map(keyFunction)
                .collect(toSet());
    }

    public static <T, K, V> Map<K, V> collectToMap(Collection<T> list, Function<T, K> keyFunction, Function<T, V> valueFunction) {
        return list.stream().collect(toMap(keyFunction, valueFunction, (v1, v2) -> v1));
    }

    public static <T, K, V> Map<K, V> collectToMap(Collection<T> list, Predicate<T> predicate,
                                                   Function<T, K> keyFunction, Function<T, V> valueFunction) {
        return list.stream()
                .filter(predicate)
                .collect(toMap(keyFunction, valueFunction, (v1, v2) -> v1));
    }

    public static <T> T getOrDefault(T t, T defaultValue) {
        if (t == null) return defaultValue;
        return t;
    }

    public static <T> List<T> shuffle(List<T> list) {
        Collections.shuffle(list);
        return list;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static <T> List<T> getOrDefaultList(Class<T> tClass, JSON json, List<T> list) {
        if (json == null || json.data() == null) return list;
        return new JsonArray(json.data()).mapTo(tClass);
    }
}

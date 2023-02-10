package cn.cidea.core.utils;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Charlotte
 */
public class CollectionSteamUtils {

    public static <T, V extends Comparable<? super V>> V getMaxValue(Collection<T> collection, Function<T, V> valueFunc) {
        if (isEmpty(collection)) {
            return null;
        }
        T t = collection.stream().max(Comparator.comparing(valueFunc)).get();
        return valueFunc.apply(t);
    }

    public static <T, V> Set<V> mapSet(Collection<T> collection, Function<T, V> valueFunc) {
        if (isEmpty(collection)) {
            return new HashSet<>(0);
        }
        Set<V> set = collection.stream().map(valueFunc).collect(Collectors.toSet());
        return set;
    }

    private static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <R, T> Map<R, T> convertMap(Collection<T> collection, Function<T, R> pkVal) {
        if(isEmpty(collection)){
            return new HashMap<>(0);
        }
        return collection.stream().collect(Collectors.toMap(pkVal, o -> o));
    }
}

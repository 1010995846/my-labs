package cn.cidea.core.utils;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 集合流工具
 * @author CIdea
 */
public class CollSteamUtils {

    /**
     * 某项的最大值
     * @param collection
     * @param get
     * @return
     * @param <T>
     * @param <V>
     */
    public static <T, V extends Comparable<? super V>> V getMaxValue(Collection<T> collection, Function<T, V> get) {
        if (isEmpty(collection)) {
            return null;
        }
        T t = collection.stream().max(Comparator.comparing(get)).get();
        return get.apply(t);
    }

    public static <T, V> Set<V> toSet(Collection<T> collection, Function<T, V> valueFunc) {
        if (isEmpty(collection)) {
            return new HashSet<>(0);
        }
        Set<V> set = collection.stream().map(valueFunc).collect(Collectors.toSet());
        return set;
    }

    public static <R, T> Map<R, T> toMap(Collection<T> collection, Function<T, R> pkVal) {
        if(isEmpty(collection)){
            return new HashMap<>(0);
        }
        return collection.stream().collect(Collectors.toMap(pkVal, o -> o));
    }

    private static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }
}
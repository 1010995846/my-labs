package cn.cidea.framework.common.utils;

import java.util.*;
import java.util.function.Function;

/**
 * @author Charlotte
 */
public class CollectionSteamUtils {

    public static <T, V extends Comparable<? super V>> V getMaxValue(Collection<T> collection, Function<T, V> valueFunc) {
        if (isEmpty(collection)) {
            return null;
        }
        // 断言，避免告警
        assert collection.size() > 0;
        T t = collection.stream().max(Comparator.comparing(valueFunc)).get();
        return valueFunc.apply(t);
    }

    private static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }
}

package com.charlotte.lab.base.collection.iterator;

import org.springframework.util.Assert;

import java.util.*;


/**
 * @author Charlotte
 */
public class SetRemoveTest {


    public static Set<Map<String, Object>> collector = new HashSet<>();

    static {
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i);
            collector.add(map);
        }
    }

    public static void main(String[] args) {
//        removeFromStream();
//        removeWithIterator();
        removeWithIf();
    }

    public static void removeFromStream() {
        // throw java.util.ConcurrentModificationException
        collector.stream().forEach(entity -> {
            collector.remove(entity);
        });
    }

    public static void removeWithIterator() {
        // throw java.util.ConcurrentModificationException
        for (Map<String, Object> map : collector) {
            collector.remove(map);
        }
    }

    public static void removeWithIf() {
        // success
        collector.removeIf(str -> true);
        Assert.isTrue(collector.size() == 0, "not clear");
    }

}

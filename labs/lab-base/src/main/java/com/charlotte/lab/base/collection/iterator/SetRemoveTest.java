package com.charlotte.lab.base.collection.iterator;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Charlotte
 */
public class SetRemoveTest {


    public static Set<String> collector = new HashSet<>();

    static {
        for (int i = 0; i < 10; i++) {
            collector.add(String.valueOf(i));
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
        for (String str : collector) {
            collector.remove(str);
        }
    }

    public static void removeWithIf() {
        // success
        collector.removeIf(str -> true);
        Assert.isTrue(collector.size() == 0, "not clear");
    }

}

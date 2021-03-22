package com.charlotte.lab.base.collection.iterator;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Charlotte
 */
public class ListRemoveTest {


    public static List<String> collector = new ArrayList<>();

    static {
        for (int i = 0; i < 10; i++) {
            collector.add(String.valueOf(i));
        }
    }

    public static void main(String[] args) {
//        removeFromStream();
//        removeWithIterator();
        removeWithIndex();
        removeWithIndexEntity();
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

    public static void removeWithIndex() {
        // success
        for (int i = collector.size() - 1; i >= 0; i--) {
            collector.remove(i);
        }
        Assert.isTrue(collector.size() == 0, "not clear");
    }

    public static void removeWithIndexEntity() {
        // success
        for (int i = collector.size() - 1; i >= 0; i--) {
            collector.remove(i);
        }
        Assert.isTrue(collector.size() == 0, "not clear");
    }

}

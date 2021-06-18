package com.charlotte.core.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Charlotte
 */
public class SerialNumberPool {

    private int size = 10;

    private volatile int curr;

    private Set<Integer> pool = new HashSet<>();

    private synchronized Long nextId(){
        return null;
    }


}

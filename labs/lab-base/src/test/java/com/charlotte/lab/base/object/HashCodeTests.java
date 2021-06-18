package com.charlotte.lab.base.object;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class HashCodeTests {

    @Test
    public void updateProperties(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", "1");
        int hashCode1 = map.hashCode();
        map.put("id", "2");
        int hashCode2 = map.hashCode();
        System.out.println("hashCode1 = " + hashCode1);
        System.out.println("hashCode2 = " + hashCode2);
        Assert.isTrue(hashCode1 == hashCode2, "改变属性后hashCode发生了改变");
    }


}

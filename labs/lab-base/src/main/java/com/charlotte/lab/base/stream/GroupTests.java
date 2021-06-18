package com.charlotte.lab.base.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupTests {

    // groupingBy时不可以null作key
    public static void nullKey(){
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("id", "");
            list.add(map);
        }
        Map<String, List<Map<String, String>>> group =
                list.stream().collect(Collectors.groupingBy(map -> map.get("id")));
        return;
    }

    public static void main(String[] args) {
        nullKey();
    }

}

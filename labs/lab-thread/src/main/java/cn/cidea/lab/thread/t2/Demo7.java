package cn.cidea.lab.thread.t2;

import java.util.Arrays;
import java.util.List;

public class Demo7 {

    public static void main(String[] args) {

        List<Integer> values = Arrays.asList(10, 20, 30, 40);
        values.forEach(i -> {
            i = i * 2 + 1;
        });
        values.forEach(Demo7::p);
        int res = new Demo7().add(values);
        System.out.println("����Ľ��Ϊ��" + res);


    }


    public int add(List<Integer> values) {
        values.parallelStream().forEach(System.out::println);
        values.parallelStream().forEach(Demo7::p);
        return values.parallelStream().mapToInt(i -> {
            i = i * 2 + 1;
            return i;
        }).sum();
    }

    private static Integer p(Integer integer) {
        System.out.println(integer);
        return integer * 2;
    }

    public static void p() {
        System.out.println("p");
    }


}

package cn.cidea.lab.lambda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CollectionsLambdaTest {

    public static void main(String[] args) {
        List<AtomicInteger> atomicIntegerList = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            atomicIntegerList.add(new AtomicInteger(i));
            integerList.add(i);
        }

        /**
         * 匿名参数列、实现，参数列与返回值需对应
         * 单参数或但语句可省略对应外围
         */
        // Consumer< T >con 消费性 接口: void accept(T t)；
        atomicIntegerList.parallelStream().forEach(i -> {
            i.getAndAdd(10);
        });

        // Predicate< T >断言形接口-boolean test(T t)
        integerList.parallelStream().filter(i -> i > 3).collect(Collectors.toList());

        // Supplier< T >sup供给型接口 : T get()；
        // Function< T , R >fun 函数式接口 : R apply (T t)；

        System.out.println(atomicIntegerList.toString());
    }


}

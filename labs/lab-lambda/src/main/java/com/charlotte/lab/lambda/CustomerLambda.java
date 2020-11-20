package com.charlotte.lab.lambda;

public class CustomerLambda {

    public static void main(String[] args) {
        FunctionThrowable<Integer, Integer> functionThrowable = null;
        // 普通匿名内部类写法
        functionThrowable = new FunctionThrowable<Integer, Integer>() {
            @Override
            public Integer apply(Integer i) throws Exception {
                return i * 10;
            }
        };
        // 单接口的匿名内部类简化，即Lambda表达式的原形
        functionThrowable = (Integer i) -> {
            return i * 100;
        };
        // 单参数再次简化
        functionThrowable = i -> {
            return i * 1000;
        };
        // 单行语句再次简化
        functionThrowable = i -> 1 * 20;
        // `FunctionThrowable#apply(Integer i)`接口声明异常的原因：**（只限显式的异常声明）**异常声明可将Lambda的异常交由外部调用Lambda的方法处理，否则需要Lambda内部处理，因为匿名实现接口不可声明超出父类接口的异常、不可向外抛出。
        try {
            functionThrowable.apply(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

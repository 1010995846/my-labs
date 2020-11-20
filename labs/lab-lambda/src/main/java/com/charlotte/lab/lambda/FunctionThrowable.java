package com.charlotte.lab.lambda;

public interface FunctionThrowable<T, R> {

    // 此处定义异常抛出可将Lambda的异常处理交由外部处理，否则需要接口实现内部处理，因为实现的接口不可声明超出父类接口的异常
    R apply(T t) throws Exception;

//    void run();
}

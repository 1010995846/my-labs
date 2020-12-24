package com.charlotte.lab.lambda;

import com.charlotte.lab.lambda.entity.User;

public class AbstractLambdaStream<T, R, Children extends AbstractLambdaStream<T, R, Children>> implements Compare<Children, R> {


    @Override
    public Children eq(boolean condition, R column, Object val) {
        return null;
    }

    @Override
    public Children ne(boolean condition, R column, Object val) {
        return null;
    }
}

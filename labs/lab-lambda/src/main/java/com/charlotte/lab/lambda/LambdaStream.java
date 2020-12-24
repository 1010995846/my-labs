package com.charlotte.lab.lambda;

import com.charlotte.lab.lambda.entity.User;

import java.util.Map;
import java.util.function.BiPredicate;

public class LambdaStream<T> extends AbstractLambdaStream<T, SFunction<T, ?>, LambdaStream<T>> {

    public static void main(String[] args) {
        new LambdaStream<User>().eq(User::getId, "");
    }

}

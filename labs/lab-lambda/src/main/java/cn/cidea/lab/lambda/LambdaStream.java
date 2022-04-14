package cn.cidea.lab.lambda;

import cn.cidea.lab.lambda.entity.User;

public class LambdaStream<T> extends AbstractLambdaStream<T, SFunction<T, ?>, LambdaStream<T>> {

    public static void main(String[] args) {
        new LambdaStream<User>().eq(User::getId, "");
    }

}

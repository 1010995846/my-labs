package cn.cidea.lab.lambda.stream;

import cn.cidea.lab.lambda.entity.User;

import java.util.ArrayList;
import java.util.List;

public class StreamTest {

    public static void main(String[] args) {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setName("" + i);
            list.add(user);
        }

        list.stream().forEach(user -> user.setId("0"));
        return;
    }

}

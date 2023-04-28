package cn.cidea.lab.jvm.memory.error;

import java.util.regex.Pattern;

/**
 * @author: CIdea
 */
public class PatternTest {

    public void loop() {
        while (true) {
            Thread thread = new Thread(() -> {
                String birthdayRegular = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229))$";
                Pattern.matches(birthdayRegular, "20220101");
            });
            thread.start();
        }
    }

    public static void main(String[] args) {
        Integer[] ranges = new Integer[]{18, 26, 31, 41, 61};
        String[] names = new String[ranges.length + 1];
        names[0] = ranges[0] + "岁以下";
        for (int i = 1; i < ranges.length; i++) {
            names[i] = ranges[i - 1] + "-" + (ranges[i] - 1) + "岁";
        }
        names[names.length - 1] = ranges[ranges.length - 1] - 1 + "岁以上";

        System.out.println(g(ranges, names, 1));
        System.out.println(g(ranges, names, 55));
        System.out.println(g(ranges, names, 78));
        System.out.println(g(ranges, names, 18));
        System.out.println(g(ranges, names, 60));
        System.out.println(g(ranges, names, 61));
        return;
    }

    public static String g(Integer[] ranges, String[] names, Integer age) {
        for (int i = ranges.length - 1; i >= 0; i--) {
            if (age >= ranges[i]) {
                return names[i + 1];
            }
        }
        return names[0];
    }
}

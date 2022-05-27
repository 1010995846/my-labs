package cn.cidea.lab.netty;

import cn.cidea.framework.web.core.asserts.Assert;

/**
 * @author Charlotte
 */
public class AuthContext {

    private static String name;

    public static String getName() {
        return name;
    }

    public static String getAndValidName() {
        Assert.UNAUTHORIZED.isNotBlank(name);
        return name;
    }

    public static void setName(String name) {
        AuthContext.name = name;
    }


}

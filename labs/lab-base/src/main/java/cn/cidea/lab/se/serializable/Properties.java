package cn.cidea.lab.se.serializable;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * is字段的序列化实验
 * - is命名会出现什么问题
 * @author Charlotte
 */
public class Properties implements Serializable {

    /**
     * 由于序列化，禁止is命名
     */
    private boolean isSuccess;

    private Boolean isTrue;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Boolean getTrue() {
        return isTrue;
    }

    public void setTrue(Boolean aTrue) {
        isTrue = aTrue;
    }

    public String getName() {
        return "参数";
    }

    /**
     * 结论：
     * fastjson和jackson在把对象序列化成json字符串的时候，
     * 是通过反射遍历出该类中的所有getter方法，得到isSuccess()，
     * 然后根据JavaBeans规则，他会认为这是success的值。直接序列化成json；
     * 但是Gson并不是这么做的，他是通过反射遍历该类中的所有属性，并把其值序列化成json。
     * 导致：
     * is命名的字段的getter方法会少一个is，因此使用fastjson和jackson序列化时也无is，导致歧义
     * @param args
     * @throws JsonProcessingException
     */
    public static void main(String[] args) throws JsonProcessingException {
        Properties properties = new Properties();
        properties.setSuccess(true);
        properties.setTrue(true);

        // 通过getter
        // 使用fastjson序列化成字符串并输出
        System.out.println("Serializable Result With fastjson :" + JSON.toJSONString(properties));
        // {"name":"参数","success":true}
        // 使用jackson序列化成字符串并输出
        ObjectMapper om = new ObjectMapper();
        System.out.println("Serializable Result With jackson :" + om.writeValueAsString(properties));
        // {"name":"参数","success":true}

        // 通过反射
        // 使用Gson序列化成字符串并输出
        Gson gson = new Gson();
        System.out.println("Serializable Result With Gson :" + gson.toJson(properties));
        // {"isSuccess":true}
    }
}

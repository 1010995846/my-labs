package com.charlotte.core.service.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Charlotte
 */
@Data
@Component
@ConfigurationProperties(prefix = "server")
public class ServerProperty {

    private String name;
    private String port;
    private String ip;

    public static void main(String[] args) {
        int i = Integer.SIZE - 3;
        System.out.println(-1 << i);
        System.out.println(0 << i);
        System.out.println(1 << i);
        System.out.println(2 << i);
        System.out.println((-1 << i) > (0 << i));
    }

}

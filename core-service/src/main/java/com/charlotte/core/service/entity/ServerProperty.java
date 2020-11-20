package com.charlotte.core.service.entity;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "server")
public class ServerProperty {

    private String name;
    private String port;
    private String ip;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "SeverProperty{" +
                "name='" + name + '\'' +
                ", port='" + port + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}

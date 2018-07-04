package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by MintQ on 2018/7/4.
 *
 * 该实体类的目的是：针对映射配置文件中的参数设置，然后通过代码就可以获取参数值。
    其中该实体类有个注解：@ConfigurationProperties(prefix = "other")
    含义是：针对application.properties配置文件中以other开头的参数，其他开头的不去映射
 *
 */
@Component
@ConfigurationProperties(prefix = "other")
public class OtherProperties {
    private String name;
    private String eamil;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEamil() {
        return eamil;
    }

    public void setEamil(String eamil) {
        this.eamil = eamil;
    }

    @Override
    public String toString() {
        return "OtherProperties{" +
                "name='" + name + '\'' +
                ", eamil='" + eamil + '\'' +
                '}';
    }
}

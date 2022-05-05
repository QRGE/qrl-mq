package org.qrl.mq.rabbtmq.start.config;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @Author qr
 * @Date 2022/5/5-14:00
 */
@SuppressWarnings("all")
@Getter
public class RabbitmqConfig {

    private String ip;
    private String username;
    private String password;

    public RabbitmqConfig() {
        try {
            InputStream inputStream = RabbitmqConfig.class.getResourceAsStream("/rabbitmq.properties");
            Properties properties = new Properties();
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            properties.load(reader);
            this.ip = properties.getProperty("ip");
            this.password = properties.getProperty("username");
            this.username = properties.getProperty("password");
        }catch (Exception e) {
            System.out.println("读取配置文件失败");
        }
    }
}

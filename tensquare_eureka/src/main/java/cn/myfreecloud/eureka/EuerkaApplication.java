package cn.myfreecloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

//设置开启Eureka服务-服务端
@SpringBootApplication
@EnableEurekaServer
public class EuerkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EuerkaApplication.class, args);
    }
}

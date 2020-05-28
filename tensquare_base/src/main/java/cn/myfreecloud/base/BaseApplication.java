package cn.myfreecloud.base;

import util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
    }

    /**
     * 创建 分布式 ID 生成器的实例
     * @return
     */
    @Bean
    public IdWorker createIdWorker() {
        // 设置初始值
        return new IdWorker(1, 1);
    }

}

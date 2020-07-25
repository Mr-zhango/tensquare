package cn.myfreecloud.qa.client;

import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

@Component
public class LabelClientImpl implements LabelClient {

    // 如果微服务调用失败,就会开启熔断器
    @Override
    public Result findById(String id) {
        return new Result(false, StatusCode.ERROR,"服务熔断了,现在使用的是备选方案");
    }
}

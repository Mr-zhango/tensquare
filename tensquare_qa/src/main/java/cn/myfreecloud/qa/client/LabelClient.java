package cn.myfreecloud.qa.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// 声明微服务的调用 @FeignClient 声明使用feign去调用那个微服务
@FeignClient(value = "tensquare-base",fallback = LabelClientImpl.class)
public interface LabelClient {

    //value是完整的调用地址
    @RequestMapping(value = "label/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id);
}

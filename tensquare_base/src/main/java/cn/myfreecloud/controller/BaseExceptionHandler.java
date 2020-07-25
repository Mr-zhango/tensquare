package cn.myfreecloud.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import sun.security.krb5.internal.HostAddress;

import java.util.HashSet;
import java.util.Set;

//定义异常处理的Controller
@RestControllerAdvice
public class BaseExceptionHandler {

    //注解就是标识处理那些异常
    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {
        e.printStackTrace();

        return new Result(false, StatusCode.ERROR, e.getMessage());
    }

    public static void main(String[] args) {
        // 创建集群版的操作客户端


        //使用集群版的客户端操作redis

        Set<HostAndPort> hostAndPortSet = new HashSet<>();


        hostAndPortSet.add(new HostAndPort("127.0.0.1",6379));
        hostAndPortSet.add(new HostAndPort("127.0.0.1",6379));
        hostAndPortSet.add(new HostAndPort("127.0.0.1",6379));
        hostAndPortSet.add(new HostAndPort("127.0.0.1",6379));


        JedisCluster jedisCluster = new JedisCluster(hostAndPortSet);

        jedisCluster.set("a","a");

        String value = jedisCluster.get("a");




    }

}

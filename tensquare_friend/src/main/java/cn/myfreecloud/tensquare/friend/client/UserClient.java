package cn.myfreecloud.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("tensquare-user")
public interface UserClient {

    //修改关注数
    @RequestMapping(value = "user/incFollow/{userid}/{x}", method = RequestMethod.POST)
    public void incFollow(@PathVariable("userid") String userid, @PathVariable("x") Integer x);


    //修改粉丝数
    @RequestMapping(value = "user/incFans/{userid}/{x}", method = RequestMethod.POST)
    public void incFans(@PathVariable("userid") String userid, @PathVariable("x") Integer x);
}

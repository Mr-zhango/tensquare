package cn.myfreecloud.tensquare.friend.controller;

import cn.myfreecloud.tensquare.friend.client.UserClient;
import cn.myfreecloud.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserClient userClient;

    //PUT /friend/like/{friendid}/{type}  添加好友或非好友

    /**
     * 添加好友或非好友
     *
     * @param friendid
     * @param type
     * @return
     */
    @RequestMapping(value = "like/{friendid}/{type}", method = RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid, @PathVariable String type) {
        //获取Claims,从request中获取
        Claims claims = (Claims) request.getAttribute("roles_user");

        if (claims == null) {
            return new Result(false, StatusCode.ACCESSERROR, "没有权限");
        }

        //根据type判断是添加好友还是添加非好友
        if ("1".equals(type)) {
            //添加好友
            //在添加好友之前,先判断是否已经是好友,如果已经是好友,就不需要再次添加
            //添加好友的同时,先根据userid和friendid查一下,
            //如果返回不是0,表示他们已经是好友了
            if (friendService.addFriend(claims.getId(), friendid) > 0) {
                //如果已经是好友,不能再重复添加
                return new Result(false, StatusCode.REPERROR, "不能重复添加好友");
            }

            //修改用户的关注数和粉丝数
            userClient.incFans(friendid,1);
            userClient.incFollow(claims.getId(),1);


        } else {
            //添加非好友
            friendService.addNoFriend(claims.getId(), friendid);

        }


        return new Result(true, StatusCode.OK, "操作成功");
    }


    //DELETE /friend/{friendid} 删除好友

    /**
     * 删除好友
     *
     * @param friendid
     * @return
     */
    @RequestMapping(value = "{friendid}", method = RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable String friendid) {
        //获取Claims,从request中获取
        Claims claims = (Claims) request.getAttribute("roles_user");

        if (claims == null) {
            return new Result(false, StatusCode.ACCESSERROR, "没有权限");
        }


        friendService.deleteFriend(claims.getId(), friendid);


        //修改用户的关注数和粉丝数
        userClient.incFans(friendid,-1);
        userClient.incFollow(claims.getId(),-1);

        return new Result(true, StatusCode.OK, "删除成功");
    }

}

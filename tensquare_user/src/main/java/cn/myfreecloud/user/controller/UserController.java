package cn.myfreecloud.user.controller;

import cn.myfreecloud.user.pojo.User;
import cn.myfreecloud.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //直接注入当前的请求request,这种写法的作用和直接在Controller方法声明request效果一样
    //好处是把request变成Controller全局的,只声明一次即可
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;


    //修改关注数
    @RequestMapping(value = "incFollow/{userid}/{x}", method = RequestMethod.POST)
    public void incFollow(@PathVariable String userid, @PathVariable Integer x) {
        userService.incFollow(x, userid);
    }


    //修改粉丝数
    @RequestMapping(value = "incFans/{userid}/{x}", method = RequestMethod.POST)
    public void incFans(@PathVariable String userid, @PathVariable Integer x) {
        userService.incFans(x, userid);
    }

    //POST /user/login 登陆

    /**
     * 用户登陆
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(@RequestBody User user) {
        User userLogin = userService.login(user.getMobile(), user.getPassword());

        if (userLogin != null) {
            //登录成功,签发token
            String token = jwtUtil.createJWT(userLogin.getId(), userLogin.getMobile(), "user");

            Map map = new HashMap();
            map.put("mobile", userLogin.getMobile());
            map.put("token", token);

            return new Result(true, StatusCode.OK, "登录成功", map);
        } else {
            return new Result(true, StatusCode.LOGINERROR, "登录失败");
        }

    }

    //POST /user/register/{code} 注册用户

    /**
     * 注册用户
     *
     * @param user
     * @param code
     * @return
     */
    @RequestMapping(value = "register/{code}", method = RequestMethod.POST)
    public Result register(@RequestBody User user, @PathVariable String code) {
        userService.register(user, code);

        return new Result(true, StatusCode.OK, "注册成功");
    }


    //POST /user/sendsms/{mobile} 发送手机验证码

    /**
     * 发送手机验证码
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "sendsms/{mobile}", method = RequestMethod.POST)
    public Result sendsms(@PathVariable String mobile) {
        userService.sendsms(mobile);

        return new Result(true, StatusCode.OK, "验证码发送成功");
    }


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<User>(pageList.getTotalElements(), pageList
                .getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody User user) {
        userService.add(user);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id) {
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除,删除用户必须是管理员
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        ////获取token,从请求头获取
        //String header = request.getHeader("Authorization");
        //
        ////判断token是否为空
        //if (header == null) {
        //    //如果为空,表示没有登录
        //    return new Result(false, StatusCode.LOGINERROR, "没有登录");
        //}
        //
        ////判断token是否是Bearer 开头
        //if(!header.startsWith("Bearer ")) {
        //    //如果不是Bearer开头,表示token数据不正确,登录有问题
        //    return new Result(false, StatusCode.LOGINERROR, "登录错误,需要重新登录");
        //}
        //
        //try {
        //    //获取token的值,Bearer 需要去掉
        //    String token = header.substring(7);
        //    //根据token,获取claims,可以验证token的正确性,同时可以获取到权限是什么
        //    Claims claims = jwtUtil.parseJWT(token);
        //    if (claims == null) {
        //        return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        //    }
        //
        //    if(!"admin".equals(claims.get("roles"))) {
        //        //如果获取到的权限不是admin,就不是管理员
        //        //返回没有权限操作
        //        return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        //    }
        //} catch (Exception e) {
        //    e.printStackTrace();
        //    return new Result(false, StatusCode.ACCESSERROR, "权限异常");
        //}

        //从request中获取Claims,判断是否是管理员.通过roles_admin名字拿到的数据,就是管理员
        Claims claims = (Claims) request.getAttribute("roles_admin");
        if (claims == null) {
            //如果不是管理员,就没有权限
            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        }

        //如果是管理员,就可以删除用户
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}

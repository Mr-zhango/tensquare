package cn.myfreecloud.qa.controller;

import cn.myfreecloud.qa.client.LabelClient;
import cn.myfreecloud.qa.pojo.Problem;
import cn.myfreecloud.qa.service.ProblemService;
import com.netflix.discovery.converters.Auto;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private HttpServletRequest request;

    //GET /problem/waitlist/{label}/{page}/{size} 等待回答列表

    @Autowired
    private LabelClient labelClient;

    @RequestMapping(value = "lable/{id}",method = RequestMethod.GET)
    public Result findLableById(@PathVariable("id") String id){
        Result result = labelClient.findById(id);
        return result;
    }
    /**
     * 查询等待回答列表
     *
     * @param labelId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "waitlist/{labelId}/{page}/{size}", method = RequestMethod.GET)
    public Result waitlist(@PathVariable String labelId,
                           @PathVariable Integer page,
                           @PathVariable Integer size) {
        Page<Problem> pageData = problemService.waitlist(labelId, page, size);
        PageResult<Problem> pageResult = new PageResult<>(pageData.getTotalElements(),
                pageData.getContent());

        return new Result(true, StatusCode.OK, "查询成功", pageResult);

    }

    //GET /problem/hotlist/{label}/{page}/{size} 热门问答列表

    /**
     * 分页查询热门回答列表
     *
     * @param labelId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "hotlist/{labelId}/{page}/{size}", method = RequestMethod.GET)
    public Result hotlist(@PathVariable String labelId,
                          @PathVariable Integer page,
                          @PathVariable Integer size) {

        Page<Problem> pageData = problemService.hotlist(labelId, page, size);

        PageResult<Problem> pageResult = new PageResult<>(pageData.getTotalElements(),
                pageData.getContent());

        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    //GET /problem/newlist/{label}/{page}/{size} 最新问答列表

    /**
     * 分页查询最新的回答列表
     *
     * @param labelId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "newlist/{labelId}/{page}/{size}", method = RequestMethod.GET)
    public Result newlist(@PathVariable String labelId,
                          @PathVariable Integer page,
                          @PathVariable Integer size) {
        Page<Problem> pageData = problemService.newlist(labelId, page, size);

        PageResult<Problem> pageResult = new PageResult<>(pageData.getTotalElements(),
                pageData.getContent());

        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", problemService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", problemService.findById(id));
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
        Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Problem>(pageList.getTotalElements(), pageList
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
        return new Result(true, StatusCode.OK, "查询成功", problemService.findSearch(searchMap));
    }

    /**
     * 增加,发布问题之前,需要验证用户是user权限
     *
     * @param problem
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Problem problem) {
        //从request中获取Claims
        Claims claims = (Claims) request.getAttribute("roles_user");

        //如果获取到了Claims,表示就是用户权限,如果为空,没有获取到,表示用户没有权限
        if (claims == null) {
            //如果为空,表示没有权限
            return new Result(true, StatusCode.ACCESSERROR, "没有权限");
        }

        //把用户id设置到问答中
        problem.setUserid(claims.getId());
        problemService.add(problem);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param problem
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Problem problem, @PathVariable String id) {
        problem.setId(id);
        problemService.update(problem);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        problemService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}

package cn.myfreecloud.controller;


import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import cn.myfreecloud.pojo.Label;
import cn.myfreecloud.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("label")//springMVC能自动加上"/"
//添加跨域支持的注解
@CrossOrigin
public class LabelController {

    @Autowired
    private LabelService labelService;

    //POST /label/search/{page}/{size} 标签分页
    @RequestMapping(value = "search/{page}/{size}", method = RequestMethod.POST)
    public Result findPageByWhere(@RequestBody Label label,
                                  @PathVariable Integer page,
                                  @PathVariable Integer size) {
        Page<Label> pageData = labelService.findPageByWhere(label, page, size);

        PageResult<Label> pageResult = new PageResult<>(pageData.getTotalElements(),
                pageData.getContent());

        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }


    //POST /label/search 标签条件查询

    /**
     * 根据条件查询标签
     *
     * @param label
     * @return
     */
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Result findByWhere(@RequestBody Label label) {
        List<Label> list = labelService.findByWhere(label);

        return new Result(true, StatusCode.OK, "查询成功", list);
    }


    //根据id查询
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        Label label = labelService.findById(id);

        return new Result(true, StatusCode.OK, "查询成功", label);
    }


    //查询所有数据
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Label> list = labelService.findAll();
        int a = 1 / 0;
        return new Result(true, StatusCode.OK, "查询成功", list);
    }


    //新增
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label) {
        labelService.save(label);

        return new Result(true, StatusCode.OK, "新增成功");
    }

    //修改
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Result updateById(@PathVariable String id, @RequestBody Label label) {
        label.setId(id);
        labelService.updateById(label);

        return new Result(true, StatusCode.OK, "修改成功");
    }

    //删除
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id) {
        labelService.deleteById(id);

        return new Result(true, StatusCode.OK, "删除成功");
    }

}

package cn.myfreecloud.comment.controller;


import cn.myfreecloud.comment.pojo.Comment;
import cn.myfreecloud.comment.service.CommentService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Comment comment) {
        commentService.save(comment);

        return new Result(true, StatusCode.OK, "新增成功");
    }

    @RequestMapping(value = "article/{articleId}/{page}/{size}")
    public Result findByPage(@PathVariable String articleId,
                             @PathVariable Integer page,
                             @PathVariable Integer size) {
        Page<Comment> pageData = commentService.findByPage(articleId, page, size);

        PageResult<Comment> pageResult = new PageResult<>(pageData.getTotalElements(),
                pageData.getContent());

        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    @RequestMapping(value = "{commentId}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String commentId) {

        commentService.deleteById(commentId);

        return new Result(true, StatusCode.OK, "删除成功");

    }

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {

        List<Comment> list = commentService.findAll();

        return new Result(true, StatusCode.OK, "查询成功", list);

    }


}

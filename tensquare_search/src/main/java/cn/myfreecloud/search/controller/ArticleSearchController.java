package cn.myfreecloud.search.controller;


import cn.myfreecloud.search.pojo.Article;
import cn.myfreecloud.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("search")
public class ArticleSearchController {

    @Autowired
    private ArticleSearchService articleSearchService;

    //http://127.0.0.1:9007/search/article

    /**
     * 保存文章数据到索引库中
     *
     * @param article
     * @return
     */
    @RequestMapping(value = "article", method = RequestMethod.POST)
    public Result save(@RequestBody Article article) {
        articleSearchService.save(article);

        return new Result(true, StatusCode.OK, "新增成功");
    }

    //http://127.0.0.1:9007/search/article/java/1/2

    /**
     * 根据关键词分页查询文章数据
     *
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "article/{keywords}/{page}/{size}", method = RequestMethod.GET)
    public Result findByPage(@PathVariable String keywords,
                             @PathVariable Integer page,
                             @PathVariable Integer size) {
        Page<Article> pageData = articleSearchService.findByPage(keywords, page, size);

        PageResult<Article> pageResult = new PageResult<>(pageData.getTotalElements(),
                pageData.getContent());

        return new Result(true, StatusCode.OK, "查询成功", pageResult);

    }

}

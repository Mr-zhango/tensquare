package cn.myfreecloud.search.service;


import cn.myfreecloud.search.dao.ArticleSearchDao;
import cn.myfreecloud.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import util.IdWorker;

@Service
public class ArticleSearchService {

    @Autowired
    private ArticleSearchDao articleSearchDao;

    @Autowired
    private IdWorker idWorker;

    //保存文章到索引库
    public void save(Article article) {
        String id = idWorker.nextId() + "";
        article.setId(id);

        articleSearchDao.save(article);
    }

    public Page<Article> findByPage(String keywords, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<Article> pageData = articleSearchDao.findByTitleLikeOrContentLike(
                keywords, keywords, pageRequest);

        return pageData;

    }
}

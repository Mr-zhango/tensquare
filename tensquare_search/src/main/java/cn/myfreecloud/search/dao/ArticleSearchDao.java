package cn.myfreecloud.search.dao;

import cn.myfreecloud.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleSearchDao extends ElasticsearchRepository<Article,String> {

    //根据文章标题和文字内容分页查询文章
    public Page<Article> findByTitleLikeOrContentLike(String title, String content,Pageable pageable);
}

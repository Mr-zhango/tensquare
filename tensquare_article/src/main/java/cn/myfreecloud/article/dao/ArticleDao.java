package cn.myfreecloud.article.dao;

import cn.myfreecloud.article.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface ArticleDao extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {

    // 进行增删改写操作,需要加@Modifying
    // nativeQuery 表示直接使用sql语句
    @Modifying
    @Query(value = "UPDATE tb_article SET state=? WHERE id=?", nativeQuery = true)
    void examine(String state, String articleId);

    @Modifying
    @Query(value = "UPDATE tb_article SET thumbup=thumbup+1 WHERE id=?", nativeQuery = true)
    void thumbup(String articleId);
}

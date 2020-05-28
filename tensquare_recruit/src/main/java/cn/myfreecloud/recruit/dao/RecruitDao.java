package cn.myfreecloud.recruit.dao;

import cn.myfreecloud.recruit.pojo.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface RecruitDao extends JpaRepository<Recruit, String>, JpaSpecificationExecutor<Recruit> {

    public List<Recruit> findByStateOrderByCreatetimeDesc(String state);

    //Top3表示查询的是前3条
    public List<Recruit> findTop3ByStateNotOrderByCreatetimeDesc(String state);

}

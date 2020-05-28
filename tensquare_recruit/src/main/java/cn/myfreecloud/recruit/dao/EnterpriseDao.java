package cn.myfreecloud.recruit.dao;


import cn.myfreecloud.recruit.pojo.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface EnterpriseDao extends JpaRepository<Enterprise,String>, JpaSpecificationExecutor<Enterprise> {

    public List<Enterprise> findByIshot(String ishot);
}

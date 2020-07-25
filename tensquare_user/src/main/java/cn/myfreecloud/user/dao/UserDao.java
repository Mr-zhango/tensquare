package cn.myfreecloud.user.dao;

import cn.myfreecloud.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 *
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    public User findByMobile(String mobile);


    @Modifying
    @Query(value = "UPDATE tb_user SET followcount = followcount+? WHERE id = ?", nativeQuery = true)
    public void incFollow(Integer x, String userid);

    @Modifying
    @Query(value = "UPDATE tb_user SET fanscount = fanscount+? WHERE id = ?", nativeQuery = true)
    public void incFans(Integer x, String userid);
}

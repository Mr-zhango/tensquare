package cn.myfreecloud.tensquare.friend.dao;

import cn.myfreecloud.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend, String> {

    //根据用户id和好友id查询数据条数
    @Query(value = "SELECT count(1) FROM tb_friend WHERE userid=? AND friendid=?",
            nativeQuery = true)
    public int selectUseridAndFriendid(String userid, String friendid);

    //根据用户id和好友id设置islike
    @Modifying
    @Query(value = "UPDATE tb_friend SET islike=? WHERE userid=? AND friendid=?",
            nativeQuery = true)
    public void updateIslike(String islike, String userid, String friendid);

    @Modifying
    @Query(value = "DELETE FROM tb_friend WHERE userid=? AND friendid=?", nativeQuery = true)
    public void deleteFriend(String userid, String friendid);
}

package cn.myfreecloud.tensquare.friend.service;


import cn.myfreecloud.tensquare.friend.dao.FriendDao;
import cn.myfreecloud.tensquare.friend.dao.NoFriendDao;
import cn.myfreecloud.tensquare.friend.pojo.Friend;
import cn.myfreecloud.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    public int addFriend(String userid, String friendid) {

        //根据用户id和好友id查询数据条数
        int count = friendDao.selectUseridAndFriendid(userid, friendid);

        //判断查询结果是否为0,如果返回的结果是0,表示没有添加好友,就可以添加好友
        if (count == 0) {
            //在好友表中添加数据
            Friend friend = new Friend();
            friend.setUserid(userid);
            friend.setFriendid(friendid);
            friend.setIslike("0");//默认情况不是相互喜欢
            friendDao.save(friend);

            //查询并判断对方是否已经把自己加为好友
            //查询别人有没有把我加为好友,如果为大于0,表示已经加为好友,
            if (friendDao.selectUseridAndFriendid(friendid, userid) > 0) {
                //如果对方把自己加为好友,就需要修改两个地方islike都为1
                friendDao.updateIslike("1", userid, friendid);
                friendDao.updateIslike("1", friendid, userid);
            }
        }
        //返回数据条数
        return count;

    }

    public void addNoFriend(String userid, String friendid) {
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);

        noFriendDao.save(noFriend);
    }

    public void deleteFriend(String userid, String friendid) {
        //删除friend表中的好友数据
        friendDao.deleteFriend(userid, friendid);

        //修改好友的islike为0
        friendDao.updateIslike("0",friendid,userid);


        //在非好友中添加数据
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }
}

package cn.myfreecloud.spit.service;

import cn.myfreecloud.spit.dao.SpitDao;
import cn.myfreecloud.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    //根据id查询
    public Spit findById(String id) {
        Spit spit = spitDao.findById(id).get();

        return spit;
    }

    //查询所有
    public List<Spit> findAll() {
        List<Spit> list = spitDao.findAll();

        return list;
    }

    //新增
    public void save(Spit spit) {
        String id = idWorker.nextId() + "";
        spit.set_id(id);
        //发布吐槽(就是新增),需要设置初始数据
        //发布日期
        spit.setPublishtime(new Date());
        //浏览量
        spit.setVisits(0);
        //点赞数
        spit.setThumbup(0);
        //分享数
        spit.setShare(0);
        //回复数
        spit.setComment(0);
        //是否可见
        spit.setComment(1);

        //对parentid进行处理,如果有parentid给父节点回复数加一,如果没有就不用进行类似的操作
        if (spit.getParentid() != null && !"".equals(spit.getParentid())) {
            //如果有parentId,就对父节点的回复数加一
            Query query = new Query();
            //设置的条件是父的id
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            //设置回复数加一
            update.inc("comment", 1);

            mongoTemplate.updateFirst(query, update, "spit");
        }

        spitDao.save(spit);
    }

    //修改
    public void updateById(Spit spit) {
        spitDao.save(spit);
    }

    //删除
    public void deleteById(String id) {
        spitDao.deleteById(id);
    }

    public Page<Spit> comment(String parentid, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<Spit> pageData = spitDao.findByParentid(parentid, pageRequest);

        return pageData;
    }

    public void thumbup(String spitId) {

        //性能较低的点赞实现
        ////根据id查询吐槽
        //Spit spit = spitDao.findById(spitId).get();
        ////给查询到的吐槽点赞加一
        //spit.setThumbup(spit.getThumbup() + 1);
        ////修改加一后的吐槽
        //spitDao.save(spit);

        //进行点赞性能的优化,原来需要操作两次数据库,现在操作一次即可,对原有的数据进行+1

        Query query = new Query();
        //添加修改的条件
        //条件是,名字为_id,值是传递进来的吐槽的id
        query.addCriteria(Criteria.where("_id").is(spitId));

        Update update = new Update();
        //添加修改数据成什么样
        //名字为thumbup(要修改的是点赞数),值是1(每次点赞数加一)
        update.inc("thumbup", 1);

        //参数的作用:
        //参数1:进行更新的条件
        //参数2:进行更新的数据,把数据更新成什么样
        //参数3:mongodb集合(相当于MySQL数据库表)的名称
        mongoTemplate.updateFirst(query, update, "spit");


    }
}

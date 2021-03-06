package cn.myfreecloud.user.service;

import cn.myfreecloud.user.dao.UserDao;
import cn.myfreecloud.user.pojo.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param user
     */
    public void add(User user) {
        user.setId(idWorker.nextId() + "");
        userDao.save(user);
    }

    /**
     * 修改
     *
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") +
                            "%"));
                }
                // 手机号码
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get
                            ("mobile") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get
                            ("password") + "%"));
                }
                // 昵称
                if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get
                            ("nickname") + "%"));
                }
                // 性别
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") +
                            "%"));
                }
                // 头像
                if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get
                            ("avatar") + "%"));
                }
                // E-Mail
                if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get
                            ("email") + "%"));
                }
                // 兴趣
                if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get
                            ("interest") + "%"));
                }
                // 个性
                if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get
                            ("personality") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    public void sendsms(String mobile) {
        //1. 生成验证码(生成6位验证码,根据短信平台运营商决定)
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;

        //把生成的验证码打印
        System.out.println(mobile + ":" + code);

        //2. 把验证码保存到redis中,后期对比验证码使用的,设置有效时间5分钟
        redisTemplate.opsForValue().set("sms_" + mobile, code, 5L, TimeUnit.MINUTES);

        //3. 把手机号和验证码通过RabbitMQ发送出去
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("code", code);

        rabbitTemplate.convertAndSend("sms", map);

    }

    public void register(User user, String code) {
        //从redis中获取验证码
        String sysCode = redisTemplate.opsForValue().get("sms_" + user.getMobile()).toString();

        //判断redis中获取的验证码是否为空
        if (sysCode == null) {
            //如果验证码为空,表示用就没有获取过验证码
            throw new RuntimeException("请点击获取验证码按钮获取验证码");
        }

        //判断redis中的验证码和传递过来的验证码是否一致
        if (!sysCode.equals(code)) {
            //如果不一致,表示验证码错误
            throw new RuntimeException("验证码错误");
        }

        //用户验证码正确,执行注册操作
        //设置用户的初始属性
        user.setRegdate(new Date());
        user.setUpdatedate(new Date());
        user.setLastdate(new Date());
        user.setOnline(0L);
        user.setFanscount(0);
        user.setFollowcount(0);

        //给用户密码进行加密
        String newPassword = encoder.encode(user.getPassword());
        user.setPassword(newPassword);

        //保存用户
        user.setId(idWorker.nextId() + "");
        userDao.save(user);
    }

    public User login(String mobile, String password) {
        User user = userDao.findByMobile(mobile);

        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }

    }

    public void incFollow(Integer x, String userid) {
        userDao.incFollow(x,userid);
    }

    public void incFans(Integer x, String userid) {
        userDao.incFans(x,userid);
    }

    //public static void main(String[] args) {
    //	Random random = new Random();
    //	for (int i = 0; i < 100; i++) {
    //		int count = random.nextInt(3);
    //		System.out.println(count);
    //	}
    //}
}

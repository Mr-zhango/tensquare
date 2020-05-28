package cn.myfreecloud.service;



import cn.myfreecloud.dao.LabelDao;
import cn.myfreecloud.pojo.Label;
import util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;


    public Label findById(String id) {
        return labelDao.findById(id).get();
    }

    public List<Label> findAll() {
        return labelDao.findAll();
    }

    public void save(Label label) {
        //生成主键并设置
        String id = idWorker.nextId() + "";
        label.setId(id);

        labelDao.save(label);
    }

    public void updateById(Label label) {
        labelDao.save(label);
    }

    public void deleteById(String id) {
        labelDao.deleteById(id);
    }


    public List<Label> findByWhere(Label label) {
        Specification<Label> specification = createSpecification(label);
        List<Label> list = labelDao.findAll(specification);

        return list;
    }


    //创建条件对象
    private Specification<Label> createSpecification(Label label) {
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder cb) {
                //声明list容器进行存放 条件参数
                List<Predicate> predicateList = new ArrayList<>();

                //解析条件,获取Predicate对象,并且放到list集合中

                // 名字模糊查询
                if (label.getLabelname() != null && !"".equals(label.getLabelname())) {
                    Predicate predicate = cb.like(root.get("labelname").as(String.class),
                            "%" + label.getLabelname() + "%");
                    //放到list容器中
                    predicateList.add(predicate);
                }

                // 字段状态精确查询
                if (label.getState() != null && !"".equals(label.getState())) {
                    Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState());
                    predicateList.add(predicate);
                }

                //
                if (label.getRecommend() != null && !"".equals(label.getRecommend())) {
                    Predicate predicate = cb.equal(root.get("recommend").as(String.class), label.getRecommend());
                    predicateList.add(predicate);
                }

                if (label.getCount() != null) {
                    Predicate predicate = cb.equal(root.get("count").as(Long.class), label.getCount());
                    predicateList.add(predicate);
                }
                if (label.getFans() != null) {
                    Predicate predicate = cb.equal(root.get("fans").as(Long.class), label.getFans());
                    predicateList.add(predicate);
                }

                //声明存放条件的数组
                Predicate[] predicates = new Predicate[predicateList.size()];
                //把list容器中的元素放到数组中
                predicates = predicateList.toArray(predicates);

                return cb.and(predicates);

            }
        };
    }

    public Page<Label> findPageByWhere(Label label, Integer page, Integer size) {
        //准备分页数据
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Specification<Label> specification = createSpecification(label);
        Page<Label> pageData = labelDao.findAll(specification, pageRequest);

        return pageData;
    }
}

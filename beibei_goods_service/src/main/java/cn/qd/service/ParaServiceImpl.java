package cn.qd.service;

import cn.qd.dao.ParaDao;
import cn.qd.entity.Category;
import cn.qd.entity.Para;
import cn.qd.entity.Spec;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;


@Service
public class ParaServiceImpl implements ParaService {

    @Autowired
    private ParaDao paraDao;

    public List<Para> findWhere(Map searchmap) {
        return paraDao.selectByExample(buildExample(searchmap));
    }

    public Example buildExample(Map searchmap){
        Example example = new Example(Category.class);
        Example.Criteria criteria =  example.createCriteria();
        if(searchmap != null){
            if(searchmap.get("templateId") != null){
                criteria.andEqualTo("templateId", searchmap.get("templateId"));
            }
        }
        return example;
    }
}

package cn.qd.service;

import cn.qd.dao.SpecDao;
import cn.qd.dao.TemplateDao;
import cn.qd.entity.Spec;
import cn.qd.entity.Template;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = SpecService.class)
public class SpecServiceImpl implements  SpecService{

    @Autowired
    private SpecDao  specDao;

    @Autowired
    private TemplateDao templateDao;

    public List<Spec> findAll() {
        return specDao.selectAll();
    }

    @Transactional
    public void add(Spec spec) {
        specDao.insert(spec);
        Template  template = templateDao.selectByPrimaryKey(spec.getTemplateId());
        template.setSpecNum(template.getSpecNum() + 1);
        templateDao.updateByPrimaryKey(template);
    }

    @Transactional
    public void delete(Long id) {
        Spec spec = specDao.selectByPrimaryKey(id);
        Template  template = templateDao.selectByPrimaryKey(spec.getTemplateId());
        template.setSpecNum(template.getSpecNum() - 1);
        templateDao.updateByPrimaryKey(template);

        specDao.deleteByPrimaryKey(id);
    }

    public List<Spec> findWhere(Map searchmap) {
        return specDao.selectByExample(buildExample(searchmap));
    }

    public Example buildExample(Map searchmap){
        Example example = new Example(Spec.class);
        Example.Criteria criteria =  example.createCriteria();
        if(searchmap != null){
            if(searchmap.get("templateId") != null){
                criteria.andEqualTo("templateId", searchmap.get("templateId"));
            }
        }
        return example;
    }
}

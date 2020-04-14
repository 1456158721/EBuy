package cn.qd.service;

import cn.qd.dao.TemplateDao;
import cn.qd.entity.Template;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService{

    @Autowired
    private TemplateDao templateDao;

    public List<Template> findAll(){
        return templateDao.selectAll();
    }

    @Override
    public void add(Template template) {
        template.setSpecNum(0);
        template.setParaNum(0);
        templateDao.insert(template);
    }

    public Template findById(Long id){
        return templateDao.selectByPrimaryKey(id);
    }

}

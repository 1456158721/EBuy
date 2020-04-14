package cn.qd.service;

import cn.qd.dao.BrandDao;
import cn.qd.entity.Brand;
import cn.qd.model.PageModel;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements  BrandService{

    @Autowired
    private BrandDao brandDao;

    public Brand findById(Long id) {
        return brandDao.selectByPrimaryKey(id); //根据 主键查找
    }

    public List<Brand> findAll(){
        return brandDao.selectAll();
    }

    /**
     * 分页查找
     * @param page
     * @param size
     * @return
     */
    public PageModel<Brand>  findPage(Integer page, Integer size){
        PageHelper.startPage(page, size);
        Page<Brand> brands = (Page<Brand>) brandDao.selectAll();
        return new PageModel<>(brands.getTotal(), brands.getResult());
    }

    /**
     * 带条件查询
     * @param
     * @return
     */
    public List<Brand>  findWhere(Map<String, Object> params){
        Example example = buildExample(params);
        return  brandDao.selectByExample(example);
    }

    public PageModel<Brand> findList(Map<String, Object> params, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = buildExample(params);
        Page<Brand> model = (Page<Brand>) brandDao.selectByExample(example);

        return new PageModel<>(model.getTotal(), model.getResult());
    }

    public  Example buildExample(Map<String, Object> params){
        //构造一个条件
        Example example = new Example(Brand.class);
        Example.Criteria  criteria =  example.createCriteria();

        if(params != null){
            if(params.get("name") != null && !"".equals(params.get("name"))){
                criteria.andLike("name", "%"+params.get("name") + "%");
            }
            if(params.get("letter") != null && !"".equals(params.get("letter"))){
                criteria.andEqualTo("letter", params.get("letter"));
            }
        }
        return example;
    }

    public void add(Brand brand){
        brandDao.insert(brand);
    }

    public void delete(Long id){
        brandDao.deleteByPrimaryKey(id);
    }

    public void update(Brand brand){
        brandDao.updateByPrimaryKeySelective(brand);
    }

}

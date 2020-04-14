package cn.qd.service;

import cn.qd.entity.Brand;
import cn.qd.model.PageModel;

import java.util.List;
import java.util.Map;

public interface BrandService {


    public Brand findById(Long id);

    public List<Brand> findAll();

    public PageModel<Brand> findPage(Integer page, Integer size);


    public List<Brand>  findWhere(Map<String, Object> paras);

    public PageModel<Brand>   findList(Map<String, Object> paras, Integer page, Integer size);


    public void add(Brand brand);

    public void delete(Long id);

    public void update(Brand brand);

}

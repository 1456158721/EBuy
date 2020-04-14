package cn.qd.service;

import cn.qd.entity.Spec;

import java.util.List;
import java.util.Map;

public interface SpecService {

    public List<Spec>  findAll();

    public void add(Spec spec);

    public void  delete(Long id);

    public List<Spec> findWhere(Map searchmap);
}

package cn.qd.service;

import cn.qd.entity.Para;

import java.util.List;
import java.util.Map;

public interface ParaService {

    public List<Para> findWhere(Map searchmap);
}

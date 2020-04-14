package cn.qd.service;

import cn.qd.model.Goods;

public interface SpuService {

    public void saveGoods(Goods goods);

    public Goods findById(String id);
}

package cn.qd.service;

import cn.qd.entity.Sku;

import java.util.List;

public interface SkuService {

    public Sku findById(String skuId);


    public boolean deductStock(List<OrderItem> orderItemList);
}

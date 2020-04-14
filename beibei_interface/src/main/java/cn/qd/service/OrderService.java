package cn.qd.service;

import cn.qd.entity.Order;

import java.util.Map;

public interface OrderService {

    public Map<String, Object> saveOrder(Order order);
}

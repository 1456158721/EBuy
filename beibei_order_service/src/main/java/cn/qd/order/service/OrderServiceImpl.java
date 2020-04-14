package cn.qd.order.service;

import cn.qd.entity.Order;
import cn.qd.order.dao.OrderDao;
import cn.qd.order.dao.OrderItemDao;
import cn.qd.service.CartService;
import cn.qd.service.OrderItem;
import cn.qd.service.OrderService;
import cn.qd.service.SkuService;
import com.alibaba.dubbo.config.annotation.Service;
import com.qd.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CartService cartService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Transactional
    public Map<String, Object> saveOrder(Order order) {

        //1 获取选中的商品
        List<Map<String, Object>> checkedCartList = cartService.findCheckedCartList(order.getUsername());

        List<OrderItem>  orderItemList = new ArrayList<>();
        for(Map<String, Object> map : checkedCartList){
            OrderItem orderItem = (OrderItem)map.get("item");
            orderItemList.add(orderItem);
        }

        //2 扣减库存
        if(!skuService.deductStock(orderItemList)){
            throw new RuntimeException("扣减库存失败!");
        }
        //3 保存订单表
        order.setId(idWorker.nextId()+"");
        int totalNum = 0;
        int totalMoney = 0;
        for(OrderItem orderItem : orderItemList){
            totalNum += orderItem.getNum();
            totalMoney += orderItem.getMoney();
        }
        order.setTotalNum(totalNum);
        order.setTotalMoney(totalMoney);
        order.setPayMoney(totalMoney);

        order.setCreateTime( new Date());
        order.setOrderStatus("0");//订单
        order.setPayStatus("0");//支付
        order.setConsignStatus("0");//发货状态

        orderDao.insert(order);

        //4 保存订单明细
        for(OrderItem orderItem : orderItemList){
            orderItem.setId(idWorker.nextId()+"");
            orderItem.setOrderId(order.getId());
            orderItem.setPayMoney(orderItem.getMoney());
            orderItemDao.insert(orderItem);
        }

        //5 清除购物车
        cartService.clearCartList(order.getUsername());

        Map map=new HashMap();
        map.put("ordersn",order.getId());
        map.put("money",  order.getPayMoney());
        return map;
    }
}

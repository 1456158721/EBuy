package cn.qd.order.controller;


import cn.qd.entity.Order;
import cn.qd.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @PostMapping("/saveOrder")
    @ResponseBody
    public Map<String, Object> saveOrder(@RequestBody Order order){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        order.setUsername(username);
        return orderService.saveOrder(order);
    }
    
}

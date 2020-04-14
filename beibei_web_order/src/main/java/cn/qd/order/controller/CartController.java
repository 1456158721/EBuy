package cn.qd.order.controller;

import cn.qd.model.MessageModel;
import cn.qd.service.CartService;
import com.alibaba.dubbo.config.annotation.Reference;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    @GetMapping("/cartList")
    @ResponseBody
    public List<Map<String, Object>>  findCartList(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return cartService.findCartList(name);
    }

    @GetMapping("/addItem")
    @ResponseBody
    public MessageModel addItem(@RequestParam String skuId, @RequestParam Integer num){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.addItem(name, skuId, num);
        return new MessageModel();
    }

    @GetMapping("/buy")
    public void buy(HttpServletResponse response,  @RequestParam String skuId, @RequestParam Integer num) throws IOException {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(cartService);
        cartService.addItem(name, skuId, num);

        response.sendRedirect("/cart/cartView");
    }


    @GetMapping("/cartView")
    public String cartView(){
        return "cart";
    }


    @GetMapping("/checkedCartList")
    @ResponseBody
    public List<Map<String, Object>> findCheckedCartList(){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        return cartService.findCheckedCartList(username);
    }

}

package cn.qd.order.service;

import cn.qd.entity.Sku;
import cn.qd.service.CartService;
import cn.qd.service.OrderItem;
import cn.qd.service.SkuService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qd.util.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private SkuService skuService;

    public List<Map<String, Object>> findCartList(String username) {
        List<Map<String, Object>>  list = (List<Map<String, Object>> )redisTemplate.boundHashOps(CacheKey.CART_LIST).get(username);
        if(list == null){
            return new ArrayList<>();
        }else{
            return list;
        }
    }

    public void addItem(String username, String skuId, int num) {

        List<Map<String, Object>>  list = findCartList(username);

        boolean flag = false;
        for(Map<String, Object> map : list){
            OrderItem orderItem = (OrderItem) map.get("item");
            if(orderItem.getSkuId().equals(skuId)){
                flag = true;
                if(orderItem.getNum() <= 0){
                    list.remove(map);
                    redisTemplate.boundHashOps(CacheKey.CART_LIST).put(username, list);
                    break;
                }
                orderItem.setNum(orderItem.getNum() + num);
                orderItem.setMoney(orderItem.getNum() * orderItem.getPrice());

                if(orderItem.getNum() <= 0){
                    list.remove(map);
                }
                redisTemplate.boundHashOps(CacheKey.CART_LIST).put(username, list);
                break;
            }
        }

        if(!flag){

            Sku sku = skuService.findById(skuId);
            if(sku == null){
                throw new RuntimeException("没有找到 该 商品.");
            }
            if(!sku.getStatus().equals("1")){
                throw new RuntimeException("商品状态异常 ");
            }
            if(sku.getNum() < num){
                throw new RuntimeException("商品数量异常 ");
            }

            OrderItem item =  new OrderItem();
            item.setSkuId(skuId);
            item.setSpuId(sku.getSpuId());
            item.setName(sku.getName());
            item.setPrice(sku.getPrice());
            item.setImage(sku.getImage());
            item.setNum(num);
            item.setMoney(item.getNum() * item.getPrice());

            item.setCategoryId3(sku.getCategoryId());


            Map<String, Object> map = new HashMap<>();
            map.put("item", item);
            map.put("checked", true);

            list.add(map);

            redisTemplate.boundHashOps(CacheKey.CART_LIST).put(username, list);
        }






    }


    public List<Map<String, Object>> findCheckedCartList(String username){

        List<Map<String, Object>> cartList = findCartList(username);

        List<Map<String, Object>> checkList = new ArrayList<>();
        for(Map<String, Object> map : cartList){
            OrderItem item = (OrderItem )map.get("item");
            Sku sku =  skuService.findById(item.getSkuId());
            item.setPrice(sku.getPrice());
            item.setMoney(sku.getPrice() * item.getNum());

            Boolean checked = (Boolean)map.get("checked");
            if(checked){
                checkList.add(map);
            }
        }
        return checkList;
    }


    /**
     * 清除购物车商品
     * @param username
     */
    public void clearCartList(String username){
        //查找购物车
        List<Map<String, Object>> cartList = findCartList(username);
        //选择的商品
        List<Map<String, Object>> checkedCartList = findCheckedCartList(username);

        cartList.removeAll(checkedCartList);

        redisTemplate.boundHashOps(CacheKey.CART_LIST).put(username,  cartList);
    }

}

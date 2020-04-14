package cn.qd.service;

import java.util.List;
import java.util.Map;

public interface CartService {


    /**
     *  [
     *      {
     *          "item": {},
     *          "checked: true
     *      }
     *
     *  ]
     *
     *
     * @param username
     * @return
     */
    public List<Map<String, Object>> findCartList(String username);


    public void addItem(String username, String skuId, int num);


    public List<Map<String, Object>> findCheckedCartList(String username);


    public void clearCartList(String username);

}

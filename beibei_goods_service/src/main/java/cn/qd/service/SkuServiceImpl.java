package cn.qd.service;

import cn.qd.dao.SkuDao;
import cn.qd.entity.Sku;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service(interfaceClass = SkuService.class)
public class SkuServiceImpl implements SkuService{

    @Autowired
    private SkuDao skuDao;

    public Sku findById(String skuId) {
        return skuDao.selectByPrimaryKey(skuId);
    }


    @Transactional
    public boolean deductStock(List<OrderItem> orderItemList) {

            for(OrderItem orderItem : orderItemList) {
                Sku sku = findById(orderItem.getSkuId());
                if(sku == null){
                    return false;
                }
                if(!sku.getStatus().equals("1")){
                    return false;
                }
                if(sku.getNum() < orderItem.getNum()){
                    return false;
                }
            }

          for(OrderItem orderItem  : orderItemList){
              skuDao.reduceNum(orderItem.getSkuId(), orderItem.getNum());
              skuDao.increaseSaleNum(orderItem.getSkuId(), orderItem.getNum());
          }
          return true;
    }


}

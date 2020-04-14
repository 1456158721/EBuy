package cn.qd.dao;

import cn.qd.entity.Sku;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface SkuDao extends Mapper<Sku> {

    @Select("update tb_sku set num=num-#{num} where skuId=#{skuId}")
    public void reduceNum(String skuId, int num);

    @Select("update tb_sku set saleNum=saleNum+#{num} where skuId=#{skuId}")
    public void increaseSaleNum(String skuId, int num);

}

package cn.qd.search.dao;

import cn.qd.entity.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface BrandDao extends Mapper<Brand> {

    @Select("select name,image from tb_brand where id in (" +
            " select brand_id from tb_category_brand where tb_category_brand.category_id in (" +
            " select id from tb_category where name =#{category}" +
            " ) )")
    public List<Map> findBrandByCategory(@Param("category") String category);

}

package cn.qd.search.dao;

import cn.qd.entity.Spec;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.w3c.dom.stylesheets.LinkStyle;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SpecDao extends Mapper<Spec> {

    @Select("select name, options from tb_spec where template_id in (" +
            "   select template_id from tb_category where name = #{category}" +
            ") order by seq")
    public List<Map>  findSpecByCategory(@Param("category") String category);
}

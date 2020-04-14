package cn.qd.service;

import cn.qd.dao.CategoryDao;
import cn.qd.entity.Category;
import com.alibaba.dubbo.config.annotation.Service;
import com.qd.util.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements  CategoryService{

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private RedisTemplate redisTemplate;

    public List<Category> findWhere(Map searchmap) {
        return categoryDao.selectByExample(buildExample(searchmap));
    }

    public  Example buildExample(Map searchmap){
        Example example = new Example(Category.class);
        Example.Criteria criteria =  example.createCriteria();
        if(searchmap != null){
            if(searchmap.get("parentId") != null){
                criteria.andEqualTo("parentId", searchmap.get("parentId"));
            }
        }
        return example;
    }

    public void add(Category category) {
        categoryDao.insert(category);
        cacheCategoryTree();
    }

    public void delete(Long id) {
        Example example = new Example(Category.class);
        Example.Criteria criteria =  example.createCriteria();
        criteria.andEqualTo("parentId", id);

        int count = categoryDao.selectCountByExample(example);
        if(count > 0){
            throw new RuntimeException("存在 下级分类 不能删除");
        }
        categoryDao.deleteByPrimaryKey(id);

        cacheCategoryTree();
    }

    public void cacheCategoryTree(){

        List<Category>  list = categoryDao.selectAll();
        System.out.println(list.size());
        List<Map>  maps =  buildCategoryTree(list, 0);

        redisTemplate.boundValueOps(CacheKey.CATEGORY_TREE).set(maps);
    }

    public List<Map> findCategoryTree(){

        List<Map>  maps = (List<Map>)redisTemplate.boundValueOps(CacheKey.CATEGORY_TREE).get();
        return maps;

       /* List<Category>  list = categoryDao.selectAll();
        System.out.println(list.size());
        List<Map>  maps =  buildCategoryTree(list, 0);
        System.out.println(maps.size());*/

    }

    @Override
    public Category findById(Integer id) {
        return categoryDao.selectByPrimaryKey(id);
    }

    public List<Map> buildCategoryTree(List<Category>  list, long parentId){
        List<Map> maps = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            Category category = list.get(i);
            if(category.getParentId().longValue() == (parentId)){
                Map<String, Object> map = new HashMap<>();
                map.put("name", category.getName());
                map.put("childs", buildCategoryTree(list, category.getId()));
                maps.add(map);
            }
        }
        return maps;
    }






}

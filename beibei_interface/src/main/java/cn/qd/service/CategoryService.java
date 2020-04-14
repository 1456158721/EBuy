package cn.qd.service;

import cn.qd.entity.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {


   public List<Category> findWhere(Map searchmap);

   public void add(Category category);

   public void delete(Long id);

   public List<Map> findCategoryTree();

   public Category findById(Integer id);


   public void cacheCategoryTree();

}

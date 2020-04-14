package cn.qd.controller;

import cn.qd.entity.Category;
import cn.qd.model.MessageModel;
import cn.qd.service.CategoryService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Reference
    private CategoryService categoryService;

    @PostMapping("/findWhere")
    public List<Category> findWhere(@RequestBody Map<String, Object> searchmap){
        System.out.println(categoryService);
        return categoryService.findWhere(searchmap);
    }

    @PostMapping("/add")
    public MessageModel add(@RequestBody  Category category){
        categoryService.add(category);
        return new MessageModel();
    }

    @GetMapping("/delete")
    public MessageModel delete(Long id){
        categoryService.delete(id);
        return new MessageModel();
    }

}

package cn.qd.protal.controller;


import cn.qd.entity.Ad;
import cn.qd.service.AdService;
import cn.qd.service.CategoryService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Reference
    private CategoryService categoryService;

    @Reference
    private AdService adService;

    @RequestMapping("/index")
    public String index(Model model){

        List<Map> categorys = categoryService.findCategoryTree();
        model.addAttribute("categorys", categorys);

        List<Ad>  list = adService.findListByPosition("web_index_lb");
        model.addAttribute("lbts", list);

        return "index";
    }


    @RequestMapping("/index2")
    @ResponseBody
    public Object index2(){

        List<Map> categorys = categoryService.findCategoryTree();
        return categorys;
    }

}

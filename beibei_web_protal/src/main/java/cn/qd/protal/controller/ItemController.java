package cn.qd.protal.controller;

import cn.qd.entity.Category;
import cn.qd.entity.Sku;
import cn.qd.entity.Spu;
import cn.qd.model.Goods;
import cn.qd.service.CategoryService;
import cn.qd.service.SpuService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private TemplateEngine templateEngine;

    @Reference
    private SpuService spuService;

    @Reference
    private CategoryService categoryService;


    @ResponseBody
    @RequestMapping("/page")
    public void createPage(@RequestParam  String id){


        Goods goods = spuService.findById(id);
        Spu spu = goods.getSpu();
        List<Sku> skuList = goods.getSkus();

        //查找分类
        Category category1 = categoryService.findById(spu.getCategory1Id());
        Category category2 = categoryService.findById(spu.getCategory2Id());
        Category category3 = categoryService.findById(spu.getCategory3Id());
        List<String> cateList = new ArrayList<>();
        cateList.add(category1.getName());
        cateList.add(category2.getName());
        cateList.add(category3.getName());



        for(Sku sku : skuList){
            //创建 一个 上下文
            Context context = new Context();
            //准备 模型数据
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("spu", spu);
            dataModel.put("sku", sku);

            dataModel.put("cateList", cateList);

            dataModel.put("spuImages", spu.getImages().split(","));
            dataModel.put("skuImages", sku.getImages().split(","));

            Map<String, String> currrentSku = JSON.parseObject(sku.getSpec(), Map.class) ;


            Map<String, List> specMap = (Map)JSON.parse(spu.getSpecItems());
            for(String key : specMap.keySet()){
                List<String> options = specMap.get(key);
                List<Map>  newOptions = new ArrayList<>();

                for(String opt : options){
                    Map<String, Object> map = new HashMap<>();
                    map.put("opt", opt);

                    //判断是否 选中
                    if(opt.equals(currrentSku.get(key))){
                        map.put("checked", true);
                    }else{
                        map.put("checked", false);
                    }
                    newOptions.add(map);
                }
                specMap.put(key, newOptions);
            }

            dataModel.put("specMap", specMap);

            context.setVariables(dataModel);
            File file = new File("c:/page");
            if(!file.exists()){
                file.mkdirs();
            }
            File savePath = new File(file, sku.getId() + ".html");
            try{
                PrintWriter pw = new PrintWriter(savePath, "UTF-8");
                templateEngine.process("item", context, pw);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }




    @ResponseBody
    @RequestMapping("/page1")
    public void createPage1(){


        //创建 一个 上下文
        Context context = new Context();

        //准备 模型数据
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name", "李四");

        context.setVariables(dataModel);


        File file = new File("c:/page");
        if(!file.exists()){
            file.mkdirs();
        }
        File savePath = new File(file, "1.html");
        try{
            PrintWriter pw = new PrintWriter(savePath, "UTF-8");
            templateEngine.process("test", context, pw);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

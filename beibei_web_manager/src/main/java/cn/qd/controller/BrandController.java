package cn.qd.controller;

import cn.qd.entity.Brand;
import cn.qd.model.MessageModel;
import cn.qd.model.PageModel;
import cn.qd.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    @GetMapping("/findById")
    public Brand findById(Long id){
        System.out.println(brandService);
        return brandService.findById(id);
    }

    @GetMapping("/findAll")
    public List<Brand> findAll(){
        return brandService.findAll();
    }


    @GetMapping("/findPage")
    public PageModel<Brand> findPage(Integer page, Integer size){
        return brandService.findPage(page, size);
    }

    @PostMapping("/findWhere")
    public List<Brand> findWhere(@RequestBody Map<String, Object> params){
        return brandService.findWhere(params);
    }

    @PostMapping("/findList")
    public PageModel<Brand> findList(@RequestBody Map<String, Object> params,
                                     Integer page, Integer size){
        return brandService.findList(params, page, size);
    }

    @PostMapping("/add")
    public MessageModel add(@RequestBody  Brand brand){
        brandService.add(brand);
        return new MessageModel();
    }

    @PostMapping("/update")
    public MessageModel update(@RequestBody  Brand brand){
        brandService.update(brand);
        return new MessageModel();
    }

    @GetMapping("/delete")
    public MessageModel delete(Long id){
        brandService.delete(id);
        return new MessageModel();
    }

}

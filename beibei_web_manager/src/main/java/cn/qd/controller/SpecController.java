package cn.qd.controller;

import cn.qd.entity.Category;
import cn.qd.entity.Spec;
import cn.qd.model.MessageModel;
import cn.qd.service.SpecService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spec")
public class SpecController {

    @Reference
    private SpecService specService;

    @GetMapping("/findAll")
    public List<Spec> findAll(){
        return specService.findAll();
    }


    @PostMapping("/add")
    public MessageModel add(@RequestBody  Spec spec){
        specService.add(spec);
        return new MessageModel();
    }

    @GetMapping("/delete/{id}")
    public MessageModel delete(@PathVariable Long id){
         specService.delete(id);
         return new MessageModel();
    }

    @PostMapping("/findWhere")
    public List<Spec> findWhere(@RequestBody Map<String, Object> searchmap){
        return specService.findWhere(searchmap);
    }

}

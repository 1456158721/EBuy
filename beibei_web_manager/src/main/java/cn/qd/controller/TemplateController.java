package cn.qd.controller;

import cn.qd.entity.Template;
import cn.qd.model.MessageModel;
import cn.qd.service.TemplateService;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/template")
public class TemplateController {

    @Reference
    private TemplateService templateService;

    @GetMapping("/findAll")
    public List<Template> findAll(){
        return templateService.findAll();
    }

    @PostMapping("/add")
    public MessageModel add(@RequestBody Template template){
        templateService.add(template);
        return new MessageModel();
    }


    @GetMapping("/findById")
    public Template findById(Long id){
        return templateService.findById(id);
    }




}

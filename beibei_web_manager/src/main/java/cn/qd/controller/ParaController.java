package cn.qd.controller;

import cn.qd.entity.Para;

import cn.qd.service.ParaService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/para")
public class ParaController {

    @Reference
    private ParaService paraService;

    @PostMapping("/findWhere")
    public List<Para> findWhere(@RequestBody Map<String, Object> searchmap){
        return paraService.findWhere(searchmap);
    }
}

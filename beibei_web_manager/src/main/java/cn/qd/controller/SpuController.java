package cn.qd.controller;

import cn.qd.model.Goods;
import cn.qd.model.MessageModel;
import cn.qd.service.SpuService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spu")
public class SpuController {

    @Reference
    private SpuService spuService;

    @RequestMapping("/save")
    public MessageModel saveGoods(@RequestBody Goods goods){
        spuService.saveGoods(goods);
        return new MessageModel();
    }


}

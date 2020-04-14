package cn.qd.init;

import cn.qd.service.AdService;
import cn.qd.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitData implements CommandLineRunner {

    @Autowired
    private AdService adService;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("缓存 广告 数据 ...");
        adService.cacheAllAD();
    }

}

package cn.qd.test;

import cn.qd.GoodServerApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = GoodServerApp.class)
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1(){
        redisTemplate.boundValueOps("valopt").set("张三");
    }

    @Test
    public void Test2(){
        String name = (String)redisTemplate.boundValueOps("valopt").get();
        System.out.println(name);
    }

    @Test
    public void test3(){
        redisTemplate.boundHashOps("hashopt").put("name1", "李四");
        redisTemplate.boundHashOps("hashopt").put("name2", "王五");
    }

    @Test
    public void test4(){
        String name1 = (String)redisTemplate.boundHashOps("hashopt").get("name1");
        String name2 = (String)redisTemplate.boundHashOps("hashopt").get("name2");
        System.out.println(name1 + " " + name2);
    }


}

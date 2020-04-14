package cn.qd.service;

import cn.qd.dao.AdDao;
import cn.qd.entity.Ad;
import com.alibaba.dubbo.config.annotation.Service;
import com.qd.util.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdServiceImpl implements AdService{

    @Autowired
    private AdDao adDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Ad> findAll() {
        return null;
    }

    @Override
    public void add(Ad ad) {

    }

    @Override
    public List<Ad> findListByPosition(String position) {

       /* Example example = new Example(Ad.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("position", position);
        criteria.andEqualTo("status", "1");

        Date date =  new Date();
        criteria.andLessThanOrEqualTo("startTime", date);
        criteria.andGreaterThanOrEqualTo("endTime", date);
        return adDao.selectByExample(example);*/

        List<Ad> list = (List<Ad>) redisTemplate.boundHashOps(CacheKey.AD).get(position);
        return list;
    }

    public void cacheByPosition(String position) {
        Example example = new Example(Ad.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("position", position);
        criteria.andEqualTo("status", "1");

        Date date =  new Date();
        criteria.andLessThanOrEqualTo("startTime", date);
        criteria.andGreaterThanOrEqualTo("endTime", date);

        List<Ad> list =  adDao.selectByExample(example);

        redisTemplate.boundHashOps(CacheKey.AD).put(position, list);
    }


    public void cacheAllAD(){
        List<String> list = new ArrayList<>();
        list.add("web_index_lb");
        //... 其他 广告

        for(int i = 0; i < list.size(); i++){
            cacheByPosition(list.get(i));
        }
    }



}

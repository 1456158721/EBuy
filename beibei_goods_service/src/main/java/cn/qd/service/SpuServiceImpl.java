package cn.qd.service;

import cn.qd.dao.*;
import cn.qd.entity.*;
import cn.qd.model.Goods;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.qd.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SpuService.class)
public class SpuServiceImpl implements SpuService{

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SpuDao spuDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private SkuDao skuDao;

    @Autowired
    private CategoryBrandDao categoryBrandDao;


    @Transactional
    public void saveGoods(Goods goods) {

        //保存 spu
        Spu spu = goods.getSpu();
        spu.setId(idWorker.nextId() + "");
        spu.setSaleNum(0);
        spu.setCommentNum(0);
        spuDao.insert(spu);

        Date d = new Date();
        //查找 分类
        Category  category =  categoryDao.selectByPrimaryKey(spu.getCategory3Id());
        //查找品牌
        Brand brand = brandDao.selectByPrimaryKey(spu.getBrandId());

        //保存 sku
        List<Sku> list = goods.getSkus();
        for(Sku sku : list){

            sku.setId(idWorker.nextId() + "");
            sku.setCreateTime(d);
            sku.setUpdateTime(d);

            sku.setSaleNum(0);
            sku.setCommentNum(0);
            sku.setSpuId(spu.getId());

            sku.setCategoryId(category.getId());
            sku.setCategoryName(category.getName());

            sku.setBrandName(brand.getName());

            //设置名字  spu 名字 + 规格列表
            String n = spu.getName();
            Map<String, String> specs = JSON.parseObject(sku.getSpec(), Map.class);
            for(String key : specs.keySet()){
                n = n + specs.get(key) + " ";
            }
            sku.setName(n);

            skuDao.insert(sku);
        }

        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setCategoryId(spu.getCategory3Id());
        categoryBrand.setBrandId(spu.getBrandId());

        long c = categoryBrandDao.selectCount(categoryBrand);
        if(c == 0){
            categoryBrandDao.insert(categoryBrand);
        }
    }

    @Override
    public Goods findById(String id) {

        Spu spu = spuDao.selectByPrimaryKey(id);
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", id);
        List<Sku> list = skuDao.selectByExample(example);

        Goods goods = new Goods();
        goods.setSpu(spu);
        goods.setSkus(list);

        return goods;
    }
}

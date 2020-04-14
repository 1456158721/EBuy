package cn.qd.model;

import cn.qd.entity.Sku;
import cn.qd.entity.Spu;

import java.io.Serializable;
import java.util.List;

public class Goods implements Serializable {

  private Spu spu;
  private List<Sku>  skus;

    public Spu getSpu() {
        return spu;
    }

    public void setSpu(Spu spu) {
        this.spu = spu;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}

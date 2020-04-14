package cn.qd.service;

import cn.qd.entity.Ad;

import java.util.List;

public interface AdService {

    public List<Ad> findAll();

    public void add(Ad ad);

    public List<Ad> findListByPosition(String position);


    public void cacheAllAD();
}

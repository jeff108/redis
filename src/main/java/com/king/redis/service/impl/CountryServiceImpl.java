package com.king.redis.service.impl;

import com.king.redis.dao.CountryMapper;
import com.king.redis.pojo.Country;
import com.king.redis.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 缓存雪崩解决方案：
 * 1、redis设置不同过期时间- - -防止相同时间失效造成雪崩
 * 2、查询数据库加锁- - - -防止热点数据失效造成雪崩
 */
@Service
@Slf4j
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryMapper countryMapper;
    @Autowired
    private StringRedisTemplate template;

   // private  volatile  boolean flag = false;

    Lock luck = new ReentrantLock();

    @Override
    public String getById(String id) {
        //先从redis获取
        String countryStr = template.opsForValue().get(id);
        if (countryStr != null) {
            log.info("从redis获取数据====================");
            return countryStr;
        }
        luck.lock();//2000个线程，1999个排队
        try {
            countryStr = template.opsForValue().get(id);
            if (countryStr != null) {
                log.info("从redis获取数据=========22222222=======");
                return countryStr;
            }
            Country country = countryMapper.selectByPrimaryKey(Integer.valueOf(id));
            template.opsForValue().set(String.valueOf(id), country.toString(),3, TimeUnit.MINUTES);
            log.info("从db获取数据===============");
            return country.toString();
        } finally {
          //  luck.unlock();
        }
    }

    public static void main(String[] args) {
        HashMap<String,String> map = new HashMap();
        map.put("key","value2");
        map.put("key1","value2");
        map.put("key","value2");
        System.out.println(Math.pow(2,3));
    }

}




















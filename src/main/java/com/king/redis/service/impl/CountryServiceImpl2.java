package com.king.redis.service.impl;

import com.king.redis.dao.CountryMapper;
import com.king.redis.pojo.Country;
import com.king.redis.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 缓存击穿解决方案：
 * 1、参数校验
 * 2、使用redis bitMap做一个布隆过滤器
 */
@Service
@Slf4j
public class CountryServiceImpl2 implements CountryService {

    @Autowired
    private CountryMapper countryMapper;
    @Autowired
    private StringRedisTemplate template;

    private List<String> goods;

    @PostConstruct
    public void init(){
        //程序启动时预加载一次，后面有商品增加，再维护进来，使用list做一个示例，生产环境使用分批查询，或者公共redis
        //伪代码，查询所有的id
        goods = countryMapper.selectAllIds();
        if(goods!=null){
            long size = (long) Math.pow(2,32);
            long index = 0;
            for (String id:goods){
                index = Math.abs(id.hashCode()%size);
                template.opsForValue().setBit("bloom_filter",index,true);
            }
        }
    }


    @Override
    public String getById(String id) {

        long size = (long) Math.pow(2,32);
        long index = Math.abs(id.hashCode()%size);
        boolean allow = template.opsForValue().getBit("bloom_filter",index);
        if(!allow){
            //数据库不存在这个商品的id,直接返回
            return null;
        }
        Country country = countryMapper.selectByPrimaryKey(id);
        return country==null?null:country.toString();
    }
}




















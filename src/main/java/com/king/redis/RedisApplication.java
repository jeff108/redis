package com.king.redis;

import com.king.redis.dao.CountryMapper;
import com.king.redis.pojo.Country;
import com.king.redis.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@RestController
@MapperScan("com.king.redis.dao")
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    @Qualifier("countryServiceImpl2")
    private CountryService countryService;


    @RequestMapping("test")
    public void test(String id){
//        Country country = new Country();
//        country.setId(3);
        String country = countryService.getById(id);
        System.out.println(country);

    }

    @RequestMapping("testDb")
    public void testDb(){
//        Country country = new Country();
//        country.setId(3);
        Country country = countryMapper.selectByPrimaryKey(3);
        System.out.println(country);

    }
    @RequestMapping("getCache")
    public String getCache(String key){
        String value = template.opsForValue().get(key);
        return value;
    }

    @RequestMapping("setCache")
    public String setCache(String key,String value){
        template.opsForValue().set(key,value,5,TimeUnit.MINUTES);
        return "succes";
    }
}

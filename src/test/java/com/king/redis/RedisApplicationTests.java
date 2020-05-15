package com.king.redis;

import com.king.redis.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
class RedisApplicationTests {

    private static final int THREADNUM=200;

    CountDownLatch cdl = new CountDownLatch(THREADNUM);
    @Autowired
    private CountryService countryService;

    @Test
    public void test(){
        Thread[] threads = new Thread[THREADNUM];
        for (int i=0;i<THREADNUM;i++){
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cdl.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    countryService.getById("5");
                }
            });
            threads[i].start();
            cdl.countDown();
        }

        for (Thread t:threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void contextLoads() {
    }

}

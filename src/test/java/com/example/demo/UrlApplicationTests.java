package com.example.demo;

import com.example.demo.controller.UrlController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlApplicationTests {
    @Autowired
    private UrlController urlController;

    @Test
    public void getShort() {
        urlController.getShort("https://blog.csdn.net/ityouknow/article/details/90206086",null);
    }

    @Test
    public void getLong(){
        urlController.getLong("http://c1n.cn/O23oG");
    }

    @Test
    public void getCount(){
        urlController.getCount("http://c1n.cn/O23oG");
    }

}

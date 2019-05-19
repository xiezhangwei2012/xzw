package com.example.demo.controller;

import com.example.demo.dao.entity.TestUrl;
import com.example.demo.dao.entity.TestUrlExample;
import com.example.demo.dao.mapper.TestUrlMapper;
import com.example.demo.util.MD5;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

@MapperScan("com.example.demo.dao.mapper")
@RestController
@RequestMapping("/urlController")
public class UrlController {
    @Autowired
    private TestUrlMapper testUrlMapper;

    //输入⻓链，可以生成短链
    @RequestMapping(value = {"/short"}, method = RequestMethod.POST)
    public String getShort(
            @RequestParam(value = "long_url", required = false) String longUrl,
            @RequestParam(value = "short_url", required = false) String shortUrl
    ){
        if (longUrl.isEmpty()){
            return "请输入网址";
        }else {
            TestUrlExample example = new TestUrlExample();
            example.createCriteria().andLongUrlEqualTo(longUrl);
            List<TestUrl> longUrls = testUrlMapper.selectByExample(example);//判断地址是否存在
            if (longUrls != null && longUrls.size() > 0 && longUrls.get(0) != null ){
                longUrls.get(0).setCount(longUrls.get(0).getCount()+1);
                testUrlMapper.updateByPrimaryKeySelective(longUrls.get(0));
                return longUrls.get(0).getShortUrl();
            }else {
                TestUrl url = new TestUrl();
                if(null != shortUrl && !shortUrl.isEmpty()){//自定义短码
                    TestUrlExample example1 = new TestUrlExample();
                    example1.createCriteria().andShortUrlEqualTo(shortUrl);
                    List<TestUrl> shortUrls = testUrlMapper.selectByExample(example1);
                    if(shortUrls != null && shortUrls.size()>0){
                        return "输入的key值已存在";
                    }
                }else{//自动生成短码
                    String[] aResult = MD5.shortUrl(longUrl);
                    Random random=new Random();
                    int j=random.nextInt(4);//产成4以内随机数
                    System.out.println("短链接:"+aResult[j]);//随机取一个作为短链
                    shortUrl =aResult[j];
                }
                url.setShortUrl(shortUrl);
                url.setLongUrl(longUrl);
                url.setCount(1);
                testUrlMapper.insertSelective(url);
                return shortUrl;

            }
        }
    }

    //访问短链，跳转到⻓链
    @RequestMapping(value = {"/getLongUrl"}, method = RequestMethod.GET)
    public String getLong(@RequestParam(value = "short_url", required = false) String shortUrl){
        if (shortUrl.isEmpty()){
            return "请输入网址";
        }else {
            TestUrlExample example = new TestUrlExample();
            List<TestUrl> longUrls = testUrlMapper.selectByExample(example);//查询短链是否存在
            if (longUrls != null && longUrls.size()>0 && longUrls.get(0) != null){
                longUrls.get(0).setCount(longUrls.get(0).getCount()+1);
                testUrlMapper.updateByPrimaryKeySelective(longUrls.get(0));
                return longUrls.get(0).getLongUrl();
            }else {
                return "短链错误";
            }
        }
    }
    //支持访问计数
    @RequestMapping(value = {"/count"}, method = RequestMethod.GET)
    public Object getCount(String url){
        if (url.isEmpty()){
            return "请输入网址";
        }else {
            TestUrlExample example = new TestUrlExample();
            List<TestUrl> longUrls = testUrlMapper.selectByExample(example);//查询短链是否存在
            if (longUrls != null && longUrls.size()>0 && longUrls.get(0) != null){
                return longUrls.get(0).getCount();
            }else {
                return "短链错误";
            }
        }
    }


}

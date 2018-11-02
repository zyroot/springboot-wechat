package com.eim.utils;

import com.eim.config.WeChatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Zy on 2018/11/2.
 */
@Component
public class RedirectUrlUtil {

    @Autowired
    private  WeChatProperties weChatProperties;

    //springboot-wechat-0.0.1-SNAPSHOT  http://app.ei-marketing.net/
    //wechat.ei-marketing.net

    private final String url = "http://wechat.ei-marketing.net/springboot-wechat-0.0.1-SNAPSHOT/wechat/authorize.do";
    private final String param = "url=http://wechat.ei-marketing.net/springboot-wechat-0.0.1-SNAPSHOT/wechat/userInfo.do&";
    private final String localhost = "url=http://localhost:8080/wechat/convert/userInfo.do&";
    private final String state = "state=http://172.17.0.13:8080/wechat/get.do";

    public  String redirectUrl(){
        String url = this.url;
        String param = this.param;
        String state = this.state;
        return  url+"?"+param+state;
    }
    public  String localredirectUrl(){
        String url = this.url;
        String param = this.localhost;
        String state = this.state;
        return  url+"?"+param+state;
    }
}

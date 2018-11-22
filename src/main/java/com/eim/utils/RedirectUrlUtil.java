package com.eim.utils;

import com.eim.config.WeChatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 组装url工具类
 * Created by Zy on 2018/11/2.
 */
@Component
public class RedirectUrlUtil {

    @Autowired
    private  WeChatProperties weChatProperties;

    //微信授权，获取code（一般不变）
//    private final String url = "http://wechat.ei-marketing.net/springboot-wechat-0.0.1-SNAPSHOT/wechat/authorize.do";
    private final String url = "http://wechat.ei-marketing.net/springboot-wechat-zy/wechat/authorize.do";
    //根据code 获取WxMpOAuth2AccessToken 跳转获取对象信息（一般不变）
//    private final String param = "url=http://wechat.ei-marketing.net/springboot-wechat-0.0.1-SNAPSHOT/wechat/userInfo.do&";
    private final String param = "url=http://wechat.ei-marketing.net/springboot-wechat-zy/wechat/userInfo.do&";
    //state传递的参数  我这里是从服务器获取对象后，重定向项目的路径（根据项目改变）
    private final String state = "state=http://172.17.0.13:80/wechat/get.do?data={data}";

    public  String redirectUrl(){
        String url = this.url;
        String param = this.param;
        String state = this.state;
        return  url+"?"+param+state;//组装数据
    }

}

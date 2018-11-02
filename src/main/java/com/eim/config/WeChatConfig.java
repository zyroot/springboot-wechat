package com.eim.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 添加wechat组件到spring容器中
 * Created by Administrator on 2018/11/1.
 */
@Component
public class WeChatConfig {


    @Autowired
    private WeChatProperties weChatProperties;

    @Bean
    public WxMpService wxMpService(){
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());//设置wxMpConfigStorage
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpInMemoryConfigStorage.setAppId(weChatProperties.getAppID());//设置appid
        wxMpInMemoryConfigStorage.setSecret(weChatProperties.getAppsecret());//设置秘钥
        return  wxMpInMemoryConfigStorage;
    }
}

package com.eim.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/11/1.
 */
@Data
@ConfigurationProperties(prefix = "wechat")
@Component
public class WeChatProperties {

    private String appID;

    private String appsecret;

    private String state;

    private String url;

    private String param;

}

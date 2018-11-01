package com.eim.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;

/**
 * Created by Administrator on 2018/11/1.
 */
@Slf4j
@Controller
public class WeChatController {

    @Autowired
    private WxMpService wxMpService;

    /**
     *
     http://wx.natappvip.cc/authorize?url=http://wx.natappvip.cc/userInfo&state=null，
     http://app.ei-marketing.net/authorize?url=http://app.ei-marketing.net/userInfo&state=null，
     用户通过访问该链接后，在上面这个userInfo方法中就可以通过@RequestParam获取到code和state，
     不过这里我们并没有state并没有传值，所以获取到也没用。获取到了code，
     就可以获取到wxMpOAuth2AccessToken，
     获取到了wxMpOAuth2AccessToken就可以获取到openId、accessToken、wxMpUser等信息了。
     */

    @RequestMapping("/authorize")
    public String authorize(@RequestParam("url")String url,
                            @RequestParam("state") String state){
        String resultUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(url));
        log.info("【微信网页授权】获取code,redirectUrl={}",resultUrl);
        return "redirect:"+resultUrl;//重定向到userInfo方法
    }

    @ResponseBody
    @RequestMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,@RequestParam("state") String state){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();

        try{
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);//根据code获取票根
        }catch(WxErrorException e){
            log.error("WxErrorException异常",e);
        }

        //获取openId
        String openId = wxMpOAuth2AccessToken.getOpenId();
        //获取accessToken
        String accessToken = wxMpOAuth2AccessToken.getAccessToken();

        // 获取微信用户的公开信息
        WxMpUser user = null;
        try {
            user = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken,null);
        }catch (Exception e){
            log.info("【获取微信用户信息】获取微信用户信息失败,user={}",user);
        }

        return user.toString();
    }
}

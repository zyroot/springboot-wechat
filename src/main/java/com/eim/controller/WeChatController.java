package com.eim.controller;


import com.eim.utils.MyHttpRequest;
import com.eim.utils.RedirectUrlUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.management.StandardEmitterMBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/1.
 */
@Slf4j
@Controller
@RequestMapping("/wechat")
public class WeChatController {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private RedirectUrlUtil redirectUrlUtil;

    /**
     http://app.ei-marketing.net/authorize?url=http://app.ei-marketing.net/userInfo&state=null，
     用户通过访问该链接后，在上面这个userInfo方法中就可以通过@RequestParam获取到code和state，
     不过这里我们并没有state并没有传值，所以获取到也没用。获取到了code，
     就可以获取到wxMpOAuth2AccessToken，
     获取到了wxMpOAuth2AccessToken就可以获取到openId、accessToken、wxMpUser等信息了。
     */

    //使用方式
    //1.书写一个最后重定向的，并接受用户信息的接口，http://192.168.160.1:8080/wechat/get.do
    @ResponseBody
    @RequestMapping(value = "/get.do",produces="text/plain;charset=UTF-8")
    public String get(WxMpUser user){
        String nickname = user.getNickname();
        System.out.println(nickname);
        return "成功+"+user.toString();
    }

    //2.调用封装的工具类(RedirectUrlUtil)，重定向到拼装的路径
    @RequestMapping("/test.do")
    public String test(){
        String redirectUrl = redirectUrlUtil.redirectUrl();
        return "redirect:"+redirectUrl;
    }


    /**
     * 服务器方法
     * state参数传递
     * @param url
     * @param state
     * @return
     */
    @RequestMapping("/authorize.do")
    public String authorize(@RequestParam("url")String url,
                            @RequestParam("state") String state){
        String resultUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(state));
        log.info("【微信网页授权】获取code,redirectUrl={}",resultUrl);
        return "redirect:"+resultUrl;//重定向到userInfo方法
    }


    /**
     * 服务器方法
     * 获取用户对象
     * @param code
     * @param state
     * @param modelMap
     * @return
     */
    @RequestMapping("/userInfo.do")
    public String userInfo(@RequestParam("code") String code, @RequestParam("state") String state, ModelMap modelMap){
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

        String nickname = user.getNickname();
        String city = user.getCity();
        String country = user.getCountry();
        String openId1 = user.getOpenId();
        String province = user.getProvince();
        Integer sex = user.getSex();
        String headImgUrl = user.getHeadImgUrl();

        modelMap.put("nickname",nickname);
        modelMap.put("city",city);
        modelMap.put("country",country);
        modelMap.put("openId",openId1);
        modelMap.put("province",province);
        modelMap.put("openId",openId1);
        modelMap.put("sex",sex);
        modelMap.put("headImgUrl",headImgUrl);

        String param = null;
        try {
            param = "?nickname="+URLEncoder.encode(nickname, "UTF-8")+
                            "&city="+URLEncoder.encode(city,"UTF-8")+
                            "&country="+URLEncoder.encode(country, "UTF-8")+
                            "&openId="+URLEncoder.encode(openId1, "UTF-8")+
                            "&province="+URLEncoder.encode(province, "UTF-8")+
                            "&sex="+URLEncoder.encode(sex+"", "UTF-8")+
                            "&headImgUrl="+URLEncoder.encode(headImgUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(param == null){
            return "";
        }
        String url = state+param;//封装参数 重定向携带参数
        return "redirect:"+url;
    }
}

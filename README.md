# springboot-wechat
微信授权dome
 //使用方式
 
     先导入util包下的 RedirectUrlUtil类
 
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


    <!--添加微信公众号依赖-->
		<dependency>
			<groupId>com.github.binarywang</groupId>
			<artifactId>weixin-java-mp</artifactId>
			<version>3.0.0</version>
		</dependency>

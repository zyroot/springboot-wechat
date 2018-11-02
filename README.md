# springboot-wechat
微信授权dome
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

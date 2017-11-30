package com.zhangxin.wechat.accesstoken;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhangxin.wechat.httpTool.HttpWalker;

public class TokenThread implements Runnable {
    public static String appId = "wxa5b010b6d65fce2d";
    //注意是静态的
    public static String appSecret = "21ee21da36f3195a5acbe6f125b517f9";
    public static AccessToken accessToken = null;

    @Override
    public void run() {
        while (true) {
            try {
                accessToken = this.getAccessToken();
                if (null != accessToken) {
                    System.out.println(accessToken.getAccessToken());
                    Thread.sleep(7000 * 1000); //获取到access_token 休眠7000秒

                } else {
                    Thread.sleep(1000 * 3); //获取的access_token为空 休眠3秒
                }
            } catch (Exception e) {
                System.out.println("发生异常：" + e.getMessage());
                e.printStackTrace();
                try {
                    Thread.sleep(1000 * 10); //发生异常休眠1秒
                } catch (Exception e1) {

                }
            }
        }
    }


    /**
     * 获取access_token
     *
     * @return
     */
    private AccessToken getAccessToken() {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", this.appId, this.appSecret);
        String result = HttpWalker.visit(url).setHeard().get().getResponseStr();
        System.out.println(result);
        JSONObject json = JSON.parseObject(result);
        AccessToken token = new AccessToken();
        token.setAccessToken(json.getString("access_token"));
        token.setExpiresin(json.getInteger("expires_in"));
        return token;

      /* NetWorkHelper netHelper = new NetWorkHelper();
        String Url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",this.appId,this.appSecret);
        String result = netHelper.getHttpsResponse(Url,"");
        System.out.println(result);
        JSONObject json = JSON.parseObject(result);
        AccessToken token = new AccessToken();
       token.setAccessToken(json.getString("access_token"));
        token.setExpiresin(json.getInteger("expires_in"));
        return token;*/
    }
}
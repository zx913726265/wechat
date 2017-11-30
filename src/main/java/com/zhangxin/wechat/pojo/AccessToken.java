package com.zhangxin.wechat.pojo;

/**
 * 描述: 微信通用接口凭证
 * 开发人员：zx
 * 创建时间：2017-11-30
 * 发布版本：V1.0
 */
public class AccessToken {
    // 获取到的凭证
    private String accessToken;
    // 凭证有效时间，单位：秒
    private int expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
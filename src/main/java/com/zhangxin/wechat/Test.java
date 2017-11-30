package com.zhangxin.wechat;

import com.alibaba.fastjson.JSONObject;
import com.zhangxin.wechat.entity.WeixinUserInfo;
import com.zhangxin.wechat.httpTool.NetWorkHelper;
import com.zhangxin.wechat.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

	private static Logger log = LoggerFactory.getLogger(Test.class);

	/**
	 * 获取用户信息
	 *
	 * @param accessToken 接口访问凭证
	 * @param openId      用户标识
	 * @return WeixinUserInfo
	 */
	public static WeixinUserInfo getUserInfo(String accessToken, String openId) {
		WeixinUserInfo weixinUserInfo = null;
		// 拼接请求地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		// 获取用户信息
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);

		if (null != jsonObject) {
			System.out.println(jsonObject);
			try {
				weixinUserInfo = new WeixinUserInfo();
				// 用户的标识
				weixinUserInfo.setOpenId(jsonObject.getString("openid"));
				// 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
				weixinUserInfo.setSubscribe(jsonObject.getInteger("subscribe"));
				// 用户关注时间
				weixinUserInfo.setSubscribeTime(jsonObject.getString("subscribe_time"));
				// 昵称
				weixinUserInfo.setNickname(jsonObject.getString("nickname"));
				// 用户的性别（1是男性，2是女性，0是未知）
				weixinUserInfo.setSex(jsonObject.getInteger("sex"));
				// 用户所在国家
				weixinUserInfo.setCountry(jsonObject.getString("country"));
				// 用户所在省份
				weixinUserInfo.setProvince(jsonObject.getString("province"));
				// 用户所在城市
				weixinUserInfo.setCity(jsonObject.getString("city"));
				// 用户的语言，简体中文为zh_CN
				weixinUserInfo.setLanguage(jsonObject.getString("language"));
				// 用户头像
				weixinUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
			} catch (Exception e) {
				if (0 == weixinUserInfo.getSubscribe()) {
					log.error("用户{}已取消关注", weixinUserInfo.getOpenId());
				} else {
					int errorCode = jsonObject.getInteger("errcode");
					String errorMsg = jsonObject.getString("errmsg");
					log.error("获取用户信息失败 errcode:{} errmsg:{}", errorCode, errorMsg);
				}
			}
		}
		return weixinUserInfo;
	}

	/**
	 * 获取用户信息
	 */
	public static void main(String args[]) {
		// 获取接口访问凭证
		String accessToken = CommonUtil.getToken("wxa5b010b6d65fce2d", "21ee21da36f3195a5acbe6f125b517f9").getAccessToken();
		System.out.println("accessToken:" + accessToken);
		WeixinUserInfo user = getUserInfo(accessToken, "opUs-0QefP44QTQ_FqJz7iumuFa8");
		System.out.println("OpenID：" + user.getOpenId());
		System.out.println("关注状态：" + user.getSubscribe());
		System.out.println("关注时间：" + user.getSubscribeTime());
		System.out.println("昵称：" + user.getNickname());
		System.out.println("性别：" + user.getSex());
		System.out.println("国家：" + user.getCountry());
		System.out.println("省份：" + user.getProvince());
		System.out.println("城市：" + user.getCity());
		System.out.println("语言：" + user.getLanguage());
		System.out.println("头像：" + user.getHeadImgUrl());

		NetWorkHelper netHelper = new NetWorkHelper();
		String Url = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=" + accessToken;
		String result = netHelper.getHttpsResponse(Url, "");
		System.out.println(result);
	}
}

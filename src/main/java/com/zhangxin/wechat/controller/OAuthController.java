package com.zhangxin.wechat.controller;

import com.zhangxin.wechat.Constant;
import com.zhangxin.wechat.pojo.SNSUserInfo;
import com.zhangxin.wechat.pojo.WeixinOauth2Token;
import com.zhangxin.wechat.util.AdvancedUtil;
import com.zhangxin.wechat.util.CommonUtil;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 描述: 来接收微信服务器传来信息
 * 开发人员： zx
 * 创建时间：2017-11-30
 * 发布版本：V1.0
 */
//@RestController
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@Controller
@EnableAutoConfiguration
public class OAuthController {

	@RequestMapping("/index")
	public ModelAndView index(){
		ModelMap model = new ModelMap();
		model.addAttribute("name", "Spring Boot");
		return new ModelAndView("index", model);
	}

	@RequestMapping("")
	public String test() {
		return "OK";
	}

	/**
	 * 授权后的回调请求处理
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("oauth")
	public void OAuth(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		// 用户同意授权后，能获取到code
		String code = request.getParameter("code");
		String state = request.getParameter("state");

		// 用户同意授权
		if (!"authdeny".equals(code)) {
			// 获取网页授权access_token
			WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(Constant.AppId, Constant.AppSecret, code);
			// 网页授权接口访问凭证
			String accessToken = weixinOauth2Token.getAccessToken();
			// 用户标识
			String openId = weixinOauth2Token.getOpenId();
			// 获取用户信息
			SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);

			// 设置要传递的参数
			request.setAttribute("snsUserInfo", snsUserInfo);
			request.setAttribute("state", state);
		}
		// 跳转到index.jsp TODO
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	public static void main(String[] args) {
		String source="http://chiyan.duapp.com/oauthServlet";
		System.out.println(CommonUtil.urlEncodeUTF8(source));
	}

	/**
	 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
	 *	需要修改的地方：
	 * （1）替换自己的AppID
	 * （2）将redirect_url换成自己的授权请求链接URL。注意这个连接需要经过UTF-8编码。
	 * （3）需要修改scope。需要弹出页面则要修改为snsapi_userinfo 。
	 *	scope参数的解释：
	 *	 1、以snsapi_base为scope发起的网页授权，是用来获取进入页面的用户的openid的，并且是静默授权并自动跳转到回调页的。用户感知的就是直接进入了回调页（往往是业务页面）
	 *	 2、以snsapi_userinfo为scope发起的网页授权，是用来获取用户的基本信息的。但这种授权需要用户手动同意，并且由于用户同意过，所以无须关注，就可在授权后获取该用户的基本信息。
	 */
}

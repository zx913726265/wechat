package com.zhangxin.wechat;

import com.zhangxin.wechat.service.CoreService;
import com.zhangxin.wechat.util.SignUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 描述: 来接收微信服务器传来信息
 * 开发人员： zx
 * 创建时间：2017-11-30
 * 发布版本：V1.0
 */
@RestController
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class WechatApplication implements EmbeddedServletContainerCustomizer {

	public static void main(String[] args) {
		try {
			ApplicationContext appctx = SpringApplication.run(WechatApplication.class, args);
			System.out.println("appctx.getBeanDefinitionCount=" + appctx.getBeanDefinitionCount());
			//((ConfigurableApplicationContext) appctx).close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(80);//设置端口号
	}

	/**
	 * 确认请求来自微信服务器
	 */
	@RequestMapping("/wechat")
	public String doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		PrintWriter out = response.getWriter();
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
			return "echostr";
		}
		out.close();
		out = null;
		return "ok";
	}

	/**
	 * 处理微信服务器发来的消息
	 */
	@RequestMapping(value = "/wechat", method = RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 消息的接收、处理、响应
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 调用核心业务类接收消息、处理消息
		String respXml = CoreService.processRequest(request);
		// 响应消息
		PrintWriter out = response.getWriter();
		out.print(respXml);
		out.close();
	}
}
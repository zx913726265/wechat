package com.zhangxin.wechat.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 描述: 来接收微信服务器传来信息
 * 开发人员： zx
 * 创建时间：2017-11-30
 * 发布版本：V1.0
 */
@RestController
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class Application {

	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
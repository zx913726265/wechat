package com.zhangxin.wechat.accesstoken;

import com.zhangxin.wechat.Constant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AccessTokenServlet")
public class AccessTokenServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        Constant.AppId = getInitParameter("appid");  //获取servlet初始参数appid和appsecret
        Constant.AppSecret = getInitParameter("appsecret");
        System.out.println("appid:"+ Constant.AppId);
        System.out.println("appSecret:"+ Constant.AppSecret);
        new Thread(new TokenThread()).start(); //启动进程
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
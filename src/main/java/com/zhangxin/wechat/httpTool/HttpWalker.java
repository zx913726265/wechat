package com.zhangxin.wechat.httpTool;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * 网络请求工具类
 * @author David
 * @since 2017年5月9日下午8:39:34
 */
public class HttpWalker {
    private String url;
    private Map<String, String> params;
    private Map<String, ByteArrayPartSource> fileParams;
    private String responseStr;
    private String encode = "utf-8";
    private HttpClient httpClient;
    private String curBody;
    private List<String> urlList;
    private Map<String, String> browersMode;

    private HttpWalker() {
        params = new HashMap<String, String>();
        urlList = new ArrayList<String>();
        httpClient = new HttpClient();
        fileParams = new HashMap<String, ByteArrayPartSource>();
    }

    public String getResponseStr() {
        return responseStr;
    }

    public static HttpWalker visit(String url) {
        HttpWalker walker = new HttpWalker();
        walker.url = url;
        return walker;
    }

    public HttpWalker addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public HttpWalker addFileParam(String key, String fileName, byte[] bytes) {
        fileParams.put(key, new ByteArrayPartSource(fileName, bytes));
        return this;
    }

    /**
     * 手动添加cookie
     *
     * @param cookie
     */
    public HttpWalker setCookies(Cookie[] cookie) {
        httpClient.getState().addCookies(cookie);
        return this;
    }

    public Cookie[] getCookies() {
        return httpClient.getState().getCookies();
    }

    public HttpWalker encode(String encode) {
        if (encode != null && "".equals(encode.trim())) {
            this.encode = encode;
        }
        return this;
    }

    public HttpWalker addParams(Map<String, String> params) {
        if (params != null && params.size() > 0) {
            for (Entry<String, String> entry : params.entrySet()) {
                addParam(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public HttpWalker setTimeOut(int newTimeoutInMilliseconds) {
        //httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(newTimeoutInMilliseconds);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, newTimeoutInMilliseconds);
        return this;
    }


    public HttpWalker putBody(String bodyStr) {
        this.curBody = bodyStr;
        return this;
    }

    /**
     * 根据指定的key获取cookie值
     *
     * @param key cookie key
     */
    public Cookie getCookie(String key) {
        Cookie[] cookies = httpClient.getState().getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (key.equals(cookies[i].getName())) {
                    return cookies[i];
                }
            }
        }
        return null;
    }

    public HttpWalker get() {
        Protocol myhttps = new Protocol("https", new SimpleSSLSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);
        HttpMethod method = new GetMethod(url);
        try {
            if (params != null && params.size() > 0) {
                List<NameValuePair> nameValues = new ArrayList<NameValuePair>(params.size());
                for (Entry<String, String> entry : params.entrySet()) {
                    nameValues.add(new NameValuePair(entry.getKey(), entry.getValue()));
                }
                method.setQueryString(nameValues.toArray(new NameValuePair[nameValues.size()]));
            }

            preHeader(method);

            httpClient.executeMethod(method);
            urlList.add(url);
            byte[] bytes = method.getResponseBody();
            if (bytes != null) {
                responseStr = new String(bytes, encode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private void preHeader(HttpMethod method) {
        if (browersMode != null && browersMode.size() > 0) {
            for (Entry<String, String> entry : browersMode.entrySet()) {
                method.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        //referer支持
        if (urlList != null && urlList.size() > 0) {
            method.addRequestHeader("Referer", urlList.get(urlList.size() - 1));
        }
        //统一追加encode
    }

    private HttpWalker postFile() {
        try {
            MultipartPostMethod multiplePostMethod = new MultipartPostMethod(url);
            for (Entry<String, String> entry : params.entrySet()) {
                multiplePostMethod.addPart(new StringPart(entry.getKey(), entry.getValue(), encode));
            }
            for (Entry<String, ByteArrayPartSource> entry : fileParams.entrySet()) {
                multiplePostMethod.addPart(new FilePart(entry.getKey(), entry.getValue()));
            }
            preHeader(multiplePostMethod);
            httpClient.executeMethod(multiplePostMethod);
            urlList.add(url);

            byte[] bytes = multiplePostMethod.getResponseBody();
            if (bytes != null) {
                responseStr = new String(bytes, encode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseStr = null;
        }

        return this;
    }

    public HttpWalker post() {
        Protocol myhttps = new Protocol("https", new SimpleSSLSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);
        //处理文件上传
        if (fileParams.size() > 0) {
            return postFile();
        }

        //处理普通post上传
        PostMethod method = new EncodePostMethod(url);

        try {
            if (params != null && params.size() > 0) {
                List<NameValuePair> nameValues = new ArrayList<NameValuePair>(params.size());
                for (Entry<String, String> entry : params.entrySet()) {
                    nameValues.add(new NameValuePair(entry.getKey(), entry.getValue()));
                }
                method.setRequestBody(nameValues.toArray(new NameValuePair[nameValues.size()]));
            }
            //请求体内容处理
            if (curBody != null && !"".equals(curBody.trim())) {
                method.setRequestEntity(new ByteArrayRequestEntity(curBody.getBytes("utf-8")));
            }

            preHeader(method);
            //cookie
            HttpConnectionManagerParams managerParams = httpClient
                    .getHttpConnectionManager().getParams();
            // 设置连接超时时间(单位毫秒)
            managerParams.setConnectionTimeout(5000);
            // 设置读数据超时时间(单位毫秒)
            managerParams.setSoTimeout(5000);
            httpClient.executeMethod(method);
            urlList.add(url);

            byte[] bytes = method.getResponseBody();
            if (bytes != null) {
                responseStr = new String(bytes, encode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public HttpWalker print() {
        System.out.println(this.responseStr);
        return this;
    }

    public HttpWalker toFile(String fileName) {
        try {
            FileWriter writer = new FileWriter(new File(fileName));
            writer.write(this.responseStr);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public HttpWalker asIE() {
        if (browersMode == null) {
            browersMode = new HashMap<String, String>();
        }
        browersMode.clear();

        browersMode.put("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
        browersMode.put("Accept-Encoding", "gzip, deflate");
        browersMode.put("Accept-Language", "zh-Hans-CN, zh-Hans; q=0.5");
        browersMode.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
        browersMode.put("Connection", "keep-alive");
        return this;
    }

    public HttpWalker asFireFox() {
        if (browersMode == null) {
            browersMode = new HashMap<String, String>();
        }
        browersMode.clear();
        browersMode.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        browersMode.put("Accept-Encoding", "gzip, deflate, br");
        browersMode.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        browersMode.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0");
        return this;
    }

    public HttpWalker asChrome() {
        if (browersMode == null) {
            browersMode = new HashMap<String, String>();
        }
        browersMode.clear();
        browersMode.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        browersMode.put("Accept-Encoding", "gzip, deflate, sdch");
        browersMode.put("Accept-Language", "zh-CN,zh;q=0.8");
        browersMode.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
        browersMode.put("Connection", "keep-alive");
        return this;
    }

    public HttpWalker setHeard() {
        if (browersMode == null) {
            browersMode = new HashMap<String, String>();
        }
        browersMode.clear();
        browersMode.put("Content-Type", "application/json");

        return this;
    }

    public HttpWalker asChromeAndStream() {
        if (browersMode == null) {
            browersMode = new HashMap<String, String>();
        }
        browersMode.clear();
        browersMode.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        browersMode.put("Accept-Encoding", "gzip, deflate, sdch");
        browersMode.put("Accept-Language", "zh-CN,zh;q=0.8");
        browersMode.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
        browersMode.put("Connection", "keep-alive");
        browersMode.put("Content-Type", "application/octet-stream;charset=UTF-8");
        return this;
    }

    public HttpWalker printCookie() {
        System.out.println(Arrays.toString(httpClient.getState().getCookies()));
        return this;
    }

    public HttpWalker clearParams() {
        params.clear();
        fileParams.clear();
        return this;
    }

    public HttpWalker jumpGet(String url) {
        this.url = url;
        get();
        return this;
    }

    public HttpWalker download(String filename) {
        Protocol myhttps = new Protocol("https", new SimpleSSLSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);

        HttpMethod method = new GetMethod(url);
        try {
            if (params != null && params.size() > 0) {
                List<NameValuePair> nameValues = new ArrayList<NameValuePair>(params.size());
                for (Entry<String, String> entry : params.entrySet()) {
                    nameValues.add(new NameValuePair(entry.getKey(), entry.getValue()));
                }
                method.setQueryString(nameValues.toArray(new NameValuePair[nameValues.size()]));
            }
            preHeader(method);
            method.addRequestHeader("", "");
            httpClient.executeMethod(method);
            method.getResponseBodyAsStream();
            byte[] bytes = method.getResponseBody();
            if (bytes != null) {
                responseStr = new String(bytes, encode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public HttpWalker addRootCookie(String key, String value) {
        httpClient.getState().addCookie(new Cookie("*.laccd.edu", key, value, "/", 360000, true));
        return this;
    }

    public HttpWalker jumpPost(String url) {
        this.url = url;
        post();
        return this;
    }


}

//Inner class for UTF-8 support  
class EncodePostMethod extends PostMethod {
    private String encodePrivate = "utf-8";

    public void encode(String encode) {
        encodePrivate = encode;
    }

    public EncodePostMethod(String url) {
        super(url);
    }

    public String getRequestCharSet() {
        //return super.getRequestCharSet();  
        return encodePrivate;
    }
}    
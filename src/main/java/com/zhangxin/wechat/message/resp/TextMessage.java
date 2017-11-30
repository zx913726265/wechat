package com.zhangxin.wechat.message.resp;

/**
 * 描述: 文本消息 </br>
 */
public class TextMessage extends BaseMessage {
    // 回复的消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
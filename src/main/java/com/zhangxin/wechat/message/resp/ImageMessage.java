package com.zhangxin.wechat.message.resp;

/**
 * 描述: 图片消息</br>
 */
public class ImageMessage extends BaseMessage {

    private com.zhangxin.wechat.message.resp.Image Image;

    public com.zhangxin.wechat.message.resp.Image getImage() {
        return Image;
    }

    public void setImage(com.zhangxin.wechat.message.resp.Image image) {
        Image = image;
    }
}
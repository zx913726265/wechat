package com.zhangxin.wechat.message.resp;

/**
 * 类名: ImageMessage </br>
 * 描述: 图片消息</br>
 * 开发人员： souvc </br>
 * 创建时间：  2015-9-30 </br>
 * 发布版本：V1.0  </br>
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
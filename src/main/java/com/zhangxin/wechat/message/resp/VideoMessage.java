package com.zhangxin.wechat.message.resp;

/**
 * 描述: 视频消息 </br>
 */
public class VideoMessage extends BaseMessage {
    // 视频
    private com.zhangxin.wechat.message.resp.Video Video;

    public com.zhangxin.wechat.message.resp.Video getVideo() {
        return Video;
    }

    public void setVideo(com.zhangxin.wechat.message.resp.Video video) {
        Video = video;
    }
}
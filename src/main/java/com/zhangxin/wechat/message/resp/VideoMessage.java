package com.zhangxin.wechat.message.resp;

/**
 * 类名: VideoMessage </br>
 * 描述: 视频消息 </br>
 * 开发人员： souvc </br>
 * 创建时间：  2015-9-30 </br>
 * 发布版本：V1.0  </br>
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
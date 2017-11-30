package com.zhangxin.wechat.message.resp;


/**
 * 类名: VoiceMessage </br>
 * 描述: 语音消息</br>
 * 开发人员： souvc </br>
 * 创建时间：  2015-9-30 </br>
 * 发布版本：V1.0  </br>
 */
public class VoiceMessage extends BaseMessage {
    // 语音
    private com.zhangxin.wechat.message.resp.Voice Voice;

    public com.zhangxin.wechat.message.resp.Voice getVoice() {
        return Voice;
    }

    public void setVoice(com.zhangxin.wechat.message.resp.Voice voice) {
        Voice = voice;
    }
}
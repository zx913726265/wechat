package com.zhangxin.wechat.message.resp;


/**
 * 描述: 语音消息</br>
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
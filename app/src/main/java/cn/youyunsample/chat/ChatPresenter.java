package cn.youyunsample.chat;

import cn.youyunsample.base.BasePresenter;
import cn.youyunsample.chat.biz.ChatRequest;
import cn.youyunsample.chat.biz.ChatRequestImpl;

/**
 * Created by YWB on 2016/6/5.
 */
public class ChatPresenter extends BasePresenter<ChatView> {

    private ChatRequest chatRequest;

    public ChatPresenter(){
        chatRequest = new ChatRequestImpl();
    }

    public void sendText(String touchId, String text){
        chatRequest.sendText();
    }
}

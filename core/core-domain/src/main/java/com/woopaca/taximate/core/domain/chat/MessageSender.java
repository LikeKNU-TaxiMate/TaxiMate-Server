package com.woopaca.taximate.core.domain.chat;

public interface MessageSender {

    /**
     * 채팅 메시지 전송(모든 참여자)
     * @param chat 채팅
     */
    void send(Chat chat);

    /**
     * 채팅 메시지 전송(특정 참여자)
     * @param chat 채팅
     * @param identifier 식별자
     */
    void sendTo(Chat chat, String identifier);
}

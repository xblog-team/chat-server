package com.xblog.chat.chat.normalchat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.xblog.chat.message.ChatMessage;

import lombok.RequiredArgsConstructor;

/**
 * 일반 채팅에 사용되는 컨트롤러
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	/**
	 * 채팅방에 채팅을 보낸다.
	 * chat/{roomId}를 구독하는 클라이언트에 STOMP 메시지를 전송한다.
	 * @since 1.0.0
	 */
	@MessageMapping("/chat/{roomId}")
	@SendTo("/topic/{roomId}")
	public ChatMessage chat(SimpMessageHeaderAccessor headerAccessor, String message) {
		return chatService.chat(headerAccessor, message);
	}

}

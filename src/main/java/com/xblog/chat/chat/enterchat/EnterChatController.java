package com.xblog.chat.chat.enterchat;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.xblog.chat.message.ChatMessage;

import lombok.RequiredArgsConstructor;

/**
 * 채팅 입장에 사용되는 컨트롤러
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class EnterChatController {

	private final EnterChatService enterChatService;

	/**
	 * 입장할 채팅방을 찾는다.
	 * @since 1.0.0
	 */
	@MessageMapping("/chat/findRoom")
	public void findRoom(SimpMessageHeaderAccessor headerAccessor) {
		enterChatService.findChatRoom(headerAccessor);
	}

	/**
	 * 채팅 입장 메시지를 요청한다.
	 * @since 1.0.0
	 */
	@MessageMapping("/chat/enter/{roomId}")
	@SendTo("/topic/{roomId}")
	public ChatMessage enterChat(@DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {
		return enterChatService.enterRoom(roomId, headerAccessor);
	}
}

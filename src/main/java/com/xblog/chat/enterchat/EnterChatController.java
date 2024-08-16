package com.xblog.chat.enterchat;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EnterChatController {

	private final EnterChatService enterChatService;

	@MessageMapping("/chat/findRoom")
	public void findRoom(SimpMessageHeaderAccessor headerAccessor) {
		enterChatService.findChatRoom(headerAccessor);
	}

	@MessageMapping("/chat/enter/{roomId}")
	@SendTo("/chat/{roomId}")
	public String enterChat(@DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {
		return enterChatService.enterRoom(roomId, headerAccessor);
	}

	@MessageMapping("/chat/enter")
	public void exit(Message message, SimpMessageHeaderAccessor headerAccessor) {

	}
}

package com.xblog.chat.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@MessageMapping("/chat/{roomId}")
	@SendTo("/topic/{roomId}")
	public String chat(SimpMessageHeaderAccessor headerAccessor, String message) {
		return chatService.chat(headerAccessor, message);
	}

}

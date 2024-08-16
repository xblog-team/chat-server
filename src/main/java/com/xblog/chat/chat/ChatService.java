package com.xblog.chat.chat;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
	private final SimpMessagingTemplate messagingTemplate;

	public String chat(SimpMessageHeaderAccessor headerAccessor, String message) {
		return headerAccessor.getSessionAttributes().get("nickname") + ": " + message;
	}
}

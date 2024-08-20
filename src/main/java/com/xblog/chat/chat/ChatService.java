package com.xblog.chat.chat;


import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import com.xblog.chat.message.ChatMessage;
import com.xblog.chat.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
	private final UserService userService;

	public ChatMessage chat(SimpMessageHeaderAccessor headerAccessor, String message) {
		String nickname = userService.getNickname(headerAccessor);

		return new ChatMessage(nickname, message);
	}
}

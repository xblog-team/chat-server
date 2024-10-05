package com.xblog.chat.chat.normalchat;


import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import com.xblog.chat.message.ChatMessage;
import com.xblog.chat.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 일반 채팅에 사용되는 서비스
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
	private final UserService userService;

	/**
	 * chatMessage 를 반환한다.
	 * @since 1.0.0
	 */
	public ChatMessage chat(SimpMessageHeaderAccessor headerAccessor, String message) {
		String nickname = userService.getNickname(headerAccessor);

		return new ChatMessage(nickname, message);
	}
}

package com.xblog.chat.chat.noticechat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.xblog.chat.message.ChatMessage;

import lombok.RequiredArgsConstructor;

/**
 * 공지 채팅에 사용되는 컨트롤러
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class NoticeChatController {

	private final NoticeChatService noticeChatService;

	/**
	 * chat/notice 를 구독하고 있는 클라이언트에 공지 채팅을 보낸다.
	 * 채팅 서비스를 이용하는 모든 클라이언트가 구독하고 있다.
	 * @since 1.0.0
	 */
	@MessageMapping("/chat/notice")
	@SendTo("/topic/notice")
	public ChatMessage notice(SimpMessageHeaderAccessor headerAccessor, String message) {
		return noticeChatService.notice(headerAccessor, message);
	}
}

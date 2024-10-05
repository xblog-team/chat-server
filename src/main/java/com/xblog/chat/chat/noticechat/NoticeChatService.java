package com.xblog.chat.chat.noticechat;

import java.util.List;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.xblog.chat.exception.NoPermissionException;
import com.xblog.chat.message.ChatMessage;
import com.xblog.chat.user.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 공지 채팅에 사용되는 서비스
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Service
@RequiredArgsConstructor
public class NoticeChatService {
	private final UserService userService;
	private final SimpMessagingTemplate messagingTemplate;

	/**
	 * 공지 채팅을 보낸다.
	 * 헤더에서 권한 검증을 하는 과정이 있으나, 게이트웨이와 연계 테스트를 하지 않아 주석처리함.
	 *
	 * @since 1.0.0
	 */
	public ChatMessage notice(SimpMessageHeaderAccessor headerAccessor, String message) {
		// List<String> roles = (List<String>) headerAccessor.getSessionAttributes().get("roles");
		// if (roles == null || !roles.contains("admin")) {
		// 	throw new NoPermissionException(headerAccessor.getSessionId());
		// }

		String nickname = userService.getNickname(headerAccessor);
		return new ChatMessage(nickname, message);
	}
}

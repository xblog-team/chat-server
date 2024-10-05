package com.xblog.chat.disconnect;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.xblog.chat.chat.enterchat.ExitChatService;
import com.xblog.chat.user.UserService;

import lombok.RequiredArgsConstructor;

/**
 * session disconnect 시 해당 세션에서 사용하던 객체의 reference 를 정리해 줄 리스너
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Component
@RequiredArgsConstructor
public class DisconnectListener implements ApplicationListener<SessionDisconnectEvent> {
	private final UserService userService;
	private final ExitChatService exitChatService;

	/**
	 * 세션 종료시 exitChatService 를 통해 Garbage 들의 reference 들을 끊어준다.
	 *
	 * @since 1.0.0
	 */
	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccessor.getSessionId();
		exitChatService.exitRoom(sessionId, headerAccessor);

		String nickname = (String) headerAccessor.getSessionAttributes().get("nickname");

		userService.exitChat(sessionId, nickname);
	}
}

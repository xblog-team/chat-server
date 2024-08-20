package com.xblog.chat.disconnect;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.xblog.chat.user.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DisconnectListener implements ApplicationListener<SessionDisconnectEvent> {
	private final UserService userService;

	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccessor.getSessionId();
		String nickname = (String) headerAccessor.getSessionAttributes().get("nickname");

		userService.exitChat(sessionId, nickname);
	}
}

package com.xblog.chat.advice;

import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.xblog.chat.exception.NoPermissionException;

import lombok.RequiredArgsConstructor;

/**
 * Controller 에서 발생한 에러를 처리해 주는 advice
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	private final SimpMessagingTemplate messagingTemplate;
	/**
	 * STOMP Protocol 을 사용하는 컨트롤러의 어드바이스느 MessageExceptionHandler 라는 어노테이션을 붙혀서 관리한다.
	 * @since 1.0.0
	 */
	@MessageExceptionHandler(NoPermissionException.class)
	public void handleNoPermissionException(NoPermissionException ex) {
		messagingTemplate.convertAndSendToUser(ex.getSessionId(), "/queue/errors", ex.getMessage(), createHeaders(ex.getSessionId()));
	}

	/**
	 * 특정 헤더를 붙히지 않았을 때 오류가 나는 현상이 있어 헤더를 붙임
	 * @since 1.0.0
	 */
	private MessageHeaders createHeaders(@Nullable String sessionId) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		if (sessionId != null)
			headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		return headerAccessor.getMessageHeaders();
	}
}
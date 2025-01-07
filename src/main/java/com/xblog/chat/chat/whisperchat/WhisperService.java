package com.xblog.chat.chat.whisperchat;

import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.xblog.chat.message.ChatMessage;
import com.xblog.chat.message.WhisperMessage;
import com.xblog.chat.user.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 귓속말 채팅에 사용되는 서비스
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Service
@RequiredArgsConstructor
public class WhisperService {
	private final SimpMessagingTemplate messagingTemplate;
	private final UserService userService;

	/**
	 * 귓속말을 보낸 세션에게는 (에게: ~) 또는 ({targetUser}가 존재하지 않습니다.)에 해당하는 메시지를 전송한다.
	 * 귓속말을 보낼 세션에는 귓속말을 전송한다.
	 *
	 * @since 1.0.0
	 */
	public void whisper(SimpMessageHeaderAccessor headerAccessor, WhisperMessage whisperMessage) {
		String nickname = userService.getNickname(headerAccessor);
		String receiver = whisperMessage.getReceiver();

		String senderSessionId = headerAccessor.getSessionId();
		String receiverSessionId = userService.getUserMap().get(receiver);

		ChatMessage messageForSender;


		if (StringUtils.hasText(receiverSessionId)) {
			messageForSender = new ChatMessage(nickname, receiver + " 에게: " + whisperMessage.getContent());
			ChatMessage message = new ChatMessage(nickname, whisperMessage.getContent());
			messagingTemplate.convertAndSendToUser(receiverSessionId, "/queue/whisper", message, createHeaders(
				receiverSessionId));
		} else {
			messageForSender = new ChatMessage(receiver, "가 존재하지 않습니다.");
		}

		messagingTemplate.convertAndSendToUser(senderSessionId, "/queue/whisper", messageForSender,
			createHeaders(senderSessionId));
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

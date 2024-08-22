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

@Service
@RequiredArgsConstructor
public class WhisperService {
	private final SimpMessagingTemplate messagingTemplate;
	private final UserService userService;

	public void whisper(SimpMessageHeaderAccessor headerAccessor, WhisperMessage whisperMessage) {
		String nickname = userService.getNickname(headerAccessor);
		String receiver = whisperMessage.getReceiver();

		String senderSessionId = headerAccessor.getSessionId();
		String receiverSessionId = userService.getUserMap().get(receiver);

		ChatMessage messageForSender;


		if (StringUtils.hasText(receiverSessionId)) {
			messageForSender = new ChatMessage(receiver, "에게: " + whisperMessage.getContent());
			ChatMessage message = new ChatMessage(nickname, whisperMessage.getContent());
			messagingTemplate.convertAndSendToUser(receiverSessionId, "/queue/whisper", message, createHeaders(
				receiverSessionId));
		} else {
			messageForSender = new ChatMessage(receiver, "가 존재하지 않습니다.");
		}

		messagingTemplate.convertAndSendToUser(senderSessionId, "/queue/whisper", messageForSender,
			createHeaders(senderSessionId));
	}

	private MessageHeaders createHeaders(@Nullable String sessionId) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		if (sessionId != null)
			headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		return headerAccessor.getMessageHeaders();
	}
}

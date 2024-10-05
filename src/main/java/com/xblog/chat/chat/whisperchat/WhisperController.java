package com.xblog.chat.chat.whisperchat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.xblog.chat.message.WhisperMessage;

import lombok.RequiredArgsConstructor;

/**
 * 귓속말 채팅에 사용되는 컨트롤러
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class WhisperController {
	private final WhisperService whisperService;

	/**
	 * chat/whisper 로 온 요청을 통해 타겟 유저에 귓속말을 전송한다.
	 *
	 * @since 1.0.0
	 */
	@MessageMapping("/chat/whisper")
	public void whisper(SimpMessageHeaderAccessor headerAccessor, WhisperMessage message) {
		whisperService.whisper(headerAccessor, message);
	}
}

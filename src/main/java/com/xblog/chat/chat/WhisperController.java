package com.xblog.chat.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.xblog.chat.message.WhisperMessage;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WhisperController {
	private final WhisperService whisperService;

	@MessageMapping("/chat/whisper")
	public void whisper(SimpMessageHeaderAccessor headerAccessor, WhisperMessage message) {
		whisperService.whisper(headerAccessor, message);
	}
}

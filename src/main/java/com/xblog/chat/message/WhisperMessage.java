package com.xblog.chat.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhisperMessage {
	private String receiver;
	private String content;
}

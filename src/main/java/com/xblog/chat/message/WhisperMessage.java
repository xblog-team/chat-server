package com.xblog.chat.message;

import lombok.Getter;
import lombok.Setter;

/**
 * 귓속말 메시지
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Getter
@Setter
public class WhisperMessage extends BaseMessage {
	private String receiver;
	private String content;

	/**
	 * 받는 사람과 메시지 내용으로 구성된다.
	 *
	 * @since 1.0.0
	 */
	public WhisperMessage(String receiver, String content) {
		super();
		this.receiver = receiver;
		this.content = content;
	}
}

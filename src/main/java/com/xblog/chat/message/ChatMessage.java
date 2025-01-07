package com.xblog.chat.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 기본 채팅 메시지 클래스
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage extends BaseMessage {
	private String sender;
	private String content;

	/**
	 * 메시지는 보낸 사람과 메시지 내용으로 구성된다.
	 * Json 형태로 보낼 수 있다.
	 *
	 * @since 1.0.0
	 */
	public ChatMessage(String sender, String content) {
		super();
		this.sender = sender;
		this.content = content;
	}
}

package com.xblog.chat.message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

/**
 * 메시지의 Upper Class
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Getter
public class BaseMessage {
	private final String time;

	/**
	 * 메시지가 서버에서 생성되는 시간을 String 으로 저장한다.
	 *
	 * @since 1.0.0
	 */
	public BaseMessage() {
		this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
	}
}

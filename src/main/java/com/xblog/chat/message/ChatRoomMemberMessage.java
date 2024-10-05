package com.xblog.chat.message;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 채팅방 멤버 리스트를 반환하는 메시지
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Getter
@Setter
public class ChatRoomMemberMessage {
	private String roomId;
	private List<String> members;
}

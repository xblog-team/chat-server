package com.xblog.chat.message;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomMemberMessage {
	private String roomId;
	private List<String> members;
}

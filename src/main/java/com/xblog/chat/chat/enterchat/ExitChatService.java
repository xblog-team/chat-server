package com.xblog.chat.chat.enterchat;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.xblog.chat.chatroom.ChatRoom;
import com.xblog.chat.message.ChatMessage;
import com.xblog.chat.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExitChatService {
	private final SimpMessagingTemplate messagingTemplate;
	private final UserService userService;

	public void exitRoom(String sessionId, SimpMessageHeaderAccessor headerAccessor) {
		String nickname = userService.getNickname(headerAccessor);
		ChatRoom room = userService.getChatRoomMap().get(sessionId);
		String roomId = room.getId();

		ChatMessage message = new ChatMessage(nickname, "님이 " + roomId + "에서 퇴장하셨습니다.");

		messagingTemplate.convertAndSend("/topic/" + roomId, message);
	}
}

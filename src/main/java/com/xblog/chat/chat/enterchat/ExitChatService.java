package com.xblog.chat.chat.enterchat;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.xblog.chat.chatroom.ChatRoom;
import com.xblog.chat.message.ChatMessage;
import com.xblog.chat.user.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 채팅 퇴장에 사용되는 서비스
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ExitChatService {
	private final SimpMessagingTemplate messagingTemplate;
	private final UserService userService;

	/**
	 * 채팅 퇴장 메시지를 채팅방에 보낸다.
	 * @since 1.0.0
	 */
	public void exitRoom(String sessionId, SimpMessageHeaderAccessor headerAccessor) {
		String nickname = userService.getNickname(headerAccessor);
		ChatRoom room = userService.getChatRoomMap().get(sessionId);
		String roomId = room.getId();

		ChatMessage message = new ChatMessage(nickname, "님이 " + roomId + "에서 퇴장하셨습니다.");

		messagingTemplate.convertAndSend("/topic/" + roomId, message);
	}
}

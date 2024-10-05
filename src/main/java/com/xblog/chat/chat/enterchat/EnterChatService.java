package com.xblog.chat.chat.enterchat;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.xblog.chat.annotation.ChatRoomMemberUpdate;
import com.xblog.chat.annotation.RoomId;
import com.xblog.chat.aspect.ExtractType;
import com.xblog.chat.chatroom.ChatRoom;
import com.xblog.chat.message.ChatMessage;
import com.xblog.chat.message.RegisterRoomMessage;
import com.xblog.chat.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 채팅 입장에 사용되는 서비스
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EnterChatService {
	private final SimpMessagingTemplate messagingTemplate;
	private final UserService userService;

	/**
	 * 입장할 채팅방을 찾는다.
	 * 닉네임 설정, 세션 Id 추출 후 빈 ChatRoom 을 찾아 입장시킨다.
	 * 입장 후 해당 세션에 부여된 닉네임과 roomId를 STOMP 메세지로 전송한다.
	 * @since 1.0.0
	 */
	public void findChatRoom(SimpMessageHeaderAccessor headerAccessor) {
		String nickname = setNickname(headerAccessor);
		String sessionId = headerAccessor.getSessionId();

		ChatRoom chatRoom = userService.getChatRoomMap().get(sessionId);
		if (Objects.isNull(chatRoom)) {
			userService.enterChat(sessionId, nickname);
			chatRoom = userService.getChatRoomMap().get(sessionId);
		}

		log.info("enterChat nickname: {}", nickname);
		String roomId = chatRoom.getId();
		log.info(roomId);

		RegisterRoomMessage registerRoomMessage = new RegisterRoomMessage(nickname, roomId);

		messagingTemplate.convertAndSendToUser(sessionId, "/queue/findRoom", registerRoomMessage, createHeaders(
			headerAccessor.getSessionId()));
	}

	/**
	 * 채팅방 입장 메시지를 반환한다.
	 * @since 1.0.0
	 */
	@ChatRoomMemberUpdate(ExtractType.BEFORE)
	public ChatMessage enterRoom(@RoomId String roomId, SimpMessageHeaderAccessor headerAccessor) {
		String nickname = (String)headerAccessor.getSessionAttributes().get("nickname");
		log.info(nickname);

		return new ChatMessage(nickname, "님이 " + roomId + "에 입장하셨습니다.");
	}

	/**
	 * 닉네임을 설정한다.
	 * 헤더에 닉네임이 왔다면 기존닉네임을 사용하고 없다면 게스트 닉네임을 설정한다.
	 * @since 1.0.0
	 */
	public String setNickname(SimpMessageHeaderAccessor headerAccessor) {
		Map<String, Object> attributes = headerAccessor.getSessionAttributes();
		String nickname;
		if (Objects.isNull(attributes)) {
			attributes = new HashMap<>();
			headerAccessor.setSessionAttributes(attributes);
		}
		if (attributes.containsKey("userId")) {
			nickname = "nickname";
		} else {
			nickname = userService.generateGuestNickname(headerAccessor.getSessionId());
		}
		attributes.put("nickname", nickname);

		return nickname;
	}

	/**
	 * 특정 헤더를 붙히지 않았을 때 오류가 나는 현상이 있어 헤더를 붙임
	 * @since 1.0.0
	 */
	private MessageHeaders createHeaders(@Nullable String sessionId) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		if (sessionId != null)
			headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		return headerAccessor.getMessageHeaders();
	}
}



package com.xblog.chat.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.xblog.chat.chatroom.ChatRoom;
import com.xblog.chat.chatroom.ChatRoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

	private final ChatRoomService chatRoomService;

	private final Map<WebSocketSession, ChatRoom> chatRoomMap = new ConcurrentHashMap<>();
	private final Map<WebSocketSession, String> guestNicknameMap = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String userId = session.getHandshakeHeaders().getFirst("user_id");
		String nickname;
		if (StringUtils.hasText(userId)) {
			nickname = getNicknameFromUserId(userId);
		} else {
			nickname = createGuestNickname();
			while (guestNicknameMap.containsValue(nickname)) {
				nickname = createGuestNickname();
			}
		}

		Map<String, Object> attributes = session.getAttributes();
		attributes.put("nickname", nickname);

		ChatRoom room = chatRoomService.findAvailableRoom();
		room.addSession(session);
		chatRoomMap.put(session, room);

		log.debug(session.getId());

		super.afterConnectionEstablished(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		ChatRoom room = chatRoomMap.get(session);
		room.removeSession(session);
		chatRoomService.removeRoomIfEmpty(room);
		chatRoomMap.remove(session);
		guestNicknameMap.remove(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		ChatRoom room = chatRoomMap.get(session);
		String payload = message.getPayload();
		String nickname = session.getAttributes().get("nickname").toString();
		for (WebSocketSession webSocketSession : room.getSessions()) {
			if (webSocketSession.isOpen()) {
				webSocketSession.sendMessage(new TextMessage(nickname + ": " + payload));
			}
		}
	}

	// Todo: OpenFeign을 통해 회원id로 유저 닉네임을 들고 올것
	public String getNicknameFromUserId(String userId) {
		return "nickName";
	}

	private String createGuestNickname() {
		return "Guest-" + (int)(Math.random() * 10000);
	}
}

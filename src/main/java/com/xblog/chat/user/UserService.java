package com.xblog.chat.user;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.xblog.chat.chatroom.ChatRoom;
import com.xblog.chat.chatroom.ChatRoomService;
import com.xblog.chat.exception.NicknameNotExistException;
import com.xblog.chat.exception.SessionAttributesNotExistException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final ChatRoomService chatRoomService;
	@Getter
	private final Map<String, ChatRoom> chatRoomMap = new ConcurrentHashMap<>();

	@Getter
	private final Map<String, String> userMap = new ConcurrentHashMap<>();

	private final Map<String, String> guestNickNames = new ConcurrentHashMap<>();

	public void enterChat(String sessionId, String nickname) {
		ChatRoom chatRoom = chatRoomService.findAvailableRoom();
		chatRoom.addSession(sessionId);
		chatRoomMap.put(sessionId, chatRoom);
		userMap.put(nickname, sessionId);
	}

	public void exitChat(String sessionId, String nickname) {
		ChatRoom room = chatRoomMap.get(sessionId);
		room.removeSession(sessionId);
		chatRoomService.removeRoomIfEmpty(room);
		chatRoomMap.remove(sessionId);
		guestNickNames.remove(sessionId);
		userMap.remove(nickname);
	}

	public String getNickname(SimpMessageHeaderAccessor headerAccessor) {
		Map<String, Object> attributes = headerAccessor.getSessionAttributes();
		if (Objects.isNull(attributes)) {
			throw new SessionAttributesNotExistException();
		}

		String nickname = (String) attributes.get("nickname");
		if (!StringUtils.hasText(nickname)) {
			throw new NicknameNotExistException();
		}

		return nickname;
	}

	public String generateGuestNickname(String sessionId) {
		String guestNickName = createGuestNickname();
		while (guestNickNames.containsValue(guestNickName)) {
			guestNickName = createGuestNickname();
		}
		guestNickNames.put(sessionId, guestNickName);
		return guestNickName;
	}

	private String createGuestNickname() {
		return "Guest-" + (int)(Math.random() * 100000);
	}
}

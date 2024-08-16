package com.xblog.chat.user;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.xblog.chat.chatroom.ChatRoom;
import com.xblog.chat.chatroom.ChatRoomService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final ChatRoomService chatRoomService;
	@Getter
	private final Map<String, ChatRoom> chatRoomMap = new ConcurrentHashMap<>();
	private final Map<String, String> guestNickNames = new ConcurrentHashMap<>();

	public void enterChat(String sessionId) {
		ChatRoom chatRoom = chatRoomService.findAvailableRoom();
		chatRoom.addSession(sessionId);
		chatRoomMap.put(sessionId, chatRoom);
	}

	public void exitChat(String sessionId) {
		ChatRoom room = chatRoomMap.get(sessionId);
		room.removeSession(sessionId);
		chatRoomService.removeRoomIfEmpty(room);
		chatRoomMap.remove(sessionId);
		guestNickNames.remove(sessionId);
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

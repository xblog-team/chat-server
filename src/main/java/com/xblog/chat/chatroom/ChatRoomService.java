package com.xblog.chat.chatroom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

/**
 * 채팅방 서비스
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Service
public class ChatRoomService {
	private static final int MAX_SESSION_SIZE = 20;
	private final Map<String, ChatRoom> rooms = new HashMap<>();

	/**
	 * 사용 가능한 방 찾기
	 *
	 * @since 1.0.0
	 */
	public ChatRoom findAvailableRoom() {
		Optional<ChatRoom> availableRoom = rooms.values().stream()
			.filter(room -> !room.isFull()).findFirst();

		return availableRoom.orElse(createChatRoom());
	}

	/**
	 * getter
	 *
	 * @since 1.0.0
	 */
	public ChatRoom getRoom(String roomId) {
		return rooms.get(roomId);
	}

	/**
	 * 채팅방 생성
	 *
	 * @since 1.0.0
	 */
	public ChatRoom createChatRoom() {
		String roomId = createChatRoomId();
		while (rooms.containsKey(roomId)) {
			roomId = createChatRoomId();
		}
		ChatRoom chatRoom = new ChatRoom(roomId, MAX_SESSION_SIZE);
		rooms.put(roomId, chatRoom);

		return chatRoom;
	}

	/**
	 * 채팅방이 비어있다면 삭제한다.
	 *
	 * @since 1.0.0
	 */
	public void removeRoomIfEmpty(ChatRoom room) {
		if (room.getSessions().isEmpty()) {
			rooms.remove(room.getId());
		}
	}

	/**
	 * 채팅방 ID를 랜덤으로 생성한다.
	 *
	 * @since 1.0.0
	 */
	public String createChatRoomId() {
		return "room-" + (int)(Math.random() * 10000);
	}

	public Set<String> getRooms() {
		return rooms.keySet();
	}
}

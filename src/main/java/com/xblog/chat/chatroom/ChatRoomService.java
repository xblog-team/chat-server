package com.xblog.chat.chatroom;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {
	private static final int MAX_SESSION_SIZE = 20;
	private final Map<String, ChatRoom> rooms = new HashMap<>();

	public ChatRoom findAvailableRoom() {
		Optional<ChatRoom> availableRoom = rooms.values().stream()
			.filter(room -> !room.isFull()).findFirst();

		return availableRoom.orElse(createChatRoom());
	}

	public ChatRoom createChatRoom() {
		String roomId = createChatRoomId();
		while (rooms.containsKey(roomId)) {
			roomId = createChatRoomId();
		}
		ChatRoom chatRoom = new ChatRoom(roomId, MAX_SESSION_SIZE);
		rooms.put(roomId, chatRoom);

		return chatRoom;
	}

	public void removeRoomIfEmpty(ChatRoom room) {
		if (room.getSessions().isEmpty()) {
			rooms.remove(room.getId());
		}
	}

	public String createChatRoomId() {
		return "room-" + (int)(Math.random() * 10000);
	}
}

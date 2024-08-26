package com.xblog.chat.chatroom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public class ChatRoom {
	private final String id;
	private final int size;
	private final Map<String, String> sessions;

	public ChatRoom(String id, int size) {
		this.id = id;
		this.size = size;
		sessions = new ConcurrentHashMap<>();
	}

	public void addSession(String sessionId, String nickname) {
		sessions.put(sessionId, nickname);
	}

	public void removeSession(String sessionId) {
		sessions.remove(sessionId);
	}

	public boolean isFull() {
		return sessions.size() >= size;
	}

	public List<String> getMembers() {
		return new ArrayList<>(sessions.values());
	}
}

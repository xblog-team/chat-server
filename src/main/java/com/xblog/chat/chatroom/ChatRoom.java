package com.xblog.chat.chatroom;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ChatRoom {
	private final String id;
	private final int size;
	private final List<String> sessions;

	public ChatRoom(String id, int size) {
		this.id = id;
		this.size = size;
		sessions = new ArrayList<>(size);
	}

	public void addSession(String sessionId) {
		sessions.add(sessionId);
	}

	public void removeSession(String sessionId) {
		sessions.remove(sessionId);
	}

	public boolean isFull() {
		return sessions.size() >= size;
	}
}

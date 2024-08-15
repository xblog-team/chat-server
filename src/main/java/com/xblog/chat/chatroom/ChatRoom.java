package com.xblog.chat.chatroom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.socket.WebSocketSession;

import lombok.Getter;

@Getter
public class ChatRoom {
	private final String id;
	private final int size;
	private final List<WebSocketSession> sessions;

	public ChatRoom(String id, int size) {
		this.id = id;
		this.size = size;
		sessions = new ArrayList<>(size);
	}

	public void addSession(WebSocketSession session) {
		sessions.add(session);
	}

	public void removeSession(WebSocketSession session) {
		sessions.remove(session);
	}

	public boolean isFull() {
		return sessions.size() >= size;
	}
}

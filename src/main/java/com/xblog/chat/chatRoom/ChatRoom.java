package com.xblog.chat.chatRoom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.socket.WebSocketSession;

public class ChatRoom {
	private String id;
	private List<WebSocketSession> sessions = new ArrayList<>();

	public ChatRoom(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public List<WebSocketSession> getSessions() {
		return sessions;
	}

	public void addSession(WebSocketSession session) {
		sessions.add(session);
	}

	public void removeSession(WebSocketSession session) {
		sessions.remove(session);
	}

	public boolean isFull(int maxSize) {
		return sessions.size() >= maxSize;
	}
}

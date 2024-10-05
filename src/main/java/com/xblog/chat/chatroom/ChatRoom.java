package com.xblog.chat.chatroom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

/**
 * 채팅방 클래스
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
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

	/**
	 * 세션 추가
	 *
	 * @since 1.0.0
	 */
	public void addSession(String sessionId, String nickname) {
		sessions.put(sessionId, nickname);
	}

	/**
	 * 세션 삭제
	 *
	 * @since 1.0.0
	 */
	public void removeSession(String sessionId) {
		sessions.remove(sessionId);
	}

	/**
	 * 방이 다 찼는지의 여부를 반환한다.
	 *
	 * @since 1.0.0
	 */
	public boolean isFull() {
		return sessions.size() >= size;
	}

	/**
	 * 세션의 리스트를 반환한다.
	 *
	 * @since 1.0.0
	 */
	public List<String> getMembers() {
		return new ArrayList<>(sessions.values());
	}
}

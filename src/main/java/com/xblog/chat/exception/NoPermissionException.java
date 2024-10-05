package com.xblog.chat.exception;

public class NoPermissionException extends RuntimeException {
	private final String sessionId;

	public NoPermissionException(String sessionId) {
		super("권한이 없습니다.");
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}
}
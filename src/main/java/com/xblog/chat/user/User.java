package com.xblog.chat.user;

import lombok.Getter;

@Getter
public class User {
	private String nickname;

	public User(String nickname) {
		this.nickname = nickname;
	}
}

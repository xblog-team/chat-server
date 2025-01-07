package com.xblog.chat.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 채팅방 입장시 클라이언트에 부여한 닉네임과 roomId를 알려주는 메시지
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRoomMessage {
	private String nickname;
	private String roomId;
}
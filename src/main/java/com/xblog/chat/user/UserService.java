package com.xblog.chat.user;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.xblog.chat.annotation.ChatRoomMemberUpdate;
import com.xblog.chat.annotation.SessionId;
import com.xblog.chat.aspect.ExtractType;
import com.xblog.chat.chatroom.ChatRoom;
import com.xblog.chat.chatroom.ChatRoomService;
import com.xblog.chat.exception.NicknameNotExistException;
import com.xblog.chat.exception.SessionAttributesNotExistException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * user 관리에 사용되는 서비스
 * user 관리에 필요한 컬렉션 관리 및 닉네임 관련 기능을 제공한다.
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserService {

	private final ChatRoomService chatRoomService;
	@Getter
	private final Map<String, ChatRoom> chatRoomMap = new ConcurrentHashMap<>();

	@Getter
	private final Map<String, String> userMap = new ConcurrentHashMap<>();

	private final Map<String, String> guestNickNames = new ConcurrentHashMap<>();

	/**
	 * 채팅방에 입장한다.
	 * 해당 세션의 객체들을 생성하여 관리할 컬렉션들에 추가한다.
	 *
	 * @since 1.0.0
	 */
	@ChatRoomMemberUpdate(ExtractType.AFTER)
	public void enterChat(@SessionId String sessionId, String nickname) {
		ChatRoom chatRoom = chatRoomService.findAvailableRoom();
		chatRoom.addSession(sessionId, nickname);
		chatRoomMap.put(sessionId, chatRoom);
		userMap.put(nickname, sessionId);
	}

	/**
	 * 채팅방을 퇴장한다.
	 * 컬렉션들에서 해당 세션이 사용하던 객체를 제거함으로 해당 세션의 레퍼런스를 모두 제거한다.
	 *
	 * @since 1.0.0
	 */
	@ChatRoomMemberUpdate(ExtractType.BEFORE)
	public void exitChat(@SessionId String sessionId, String nickname) {
		ChatRoom room = chatRoomMap.get(sessionId);
		room.removeSession(sessionId);
		chatRoomService.removeRoomIfEmpty(room);
		chatRoomMap.remove(sessionId);
		guestNickNames.remove(sessionId);
		userMap.remove(nickname);
	}

	/**
	 * 세션 속성에서 닉네임을 추출한다.
	 *
	 * @since 1.0.0
	 */
	public String getNickname(SimpMessageHeaderAccessor headerAccessor) {
		Map<String, Object> attributes = headerAccessor.getSessionAttributes();
		if (Objects.isNull(attributes)) {
			throw new SessionAttributesNotExistException();
		}

		String nickname = (String) attributes.get("nickname");
		if (!StringUtils.hasText(nickname)) {
			throw new NicknameNotExistException();
		}

		return nickname;
	}

	/**
	 * 게스트 닉네임을 생성한다.
	 *
	 * @since 1.0.0
	 */
	public String generateGuestNickname(String sessionId) {
		String guestNickName = createGuestNickname();
		while (guestNickNames.containsValue(guestNickName)) {
			guestNickName = createGuestNickname();
		}
		guestNickNames.put(sessionId, guestNickName);
		return guestNickName;
	}

	/**
	 * 게스트 닉네임을 생성한다.
	 *
	 * @since 1.0.0
	 */
	private String createGuestNickname() {
		return "Guest-" + (int)(Math.random() * 100000);
	}
}

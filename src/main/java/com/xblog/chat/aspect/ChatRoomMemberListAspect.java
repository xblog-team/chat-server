package com.xblog.chat.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.xblog.chat.annotation.ChatRoomMemberUpdate;
import com.xblog.chat.annotation.RoomId;
import com.xblog.chat.annotation.SessionId;
import com.xblog.chat.chatroom.ChatRoom;
import com.xblog.chat.chatroom.ChatRoomService;
import com.xblog.chat.user.UserService;

import lombok.RequiredArgsConstructor;


/**
 * 채팅 멤버리스트를 갱신하기 위한 aspect
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ChatRoomMemberListAspect {

	private final UserService userService;
	private final ChatRoomService chatRoomService;
	private final SimpMessagingTemplate messagingTemplate;

	/**
	 * 어노테이션의 타입 (before/after)에 따라 조인포인트 전후로 메서드의 실행 시점을 달리한다.
	 * @since 1.0.0
	 */
	@Around("@annotation(chatRoomMemberUpdate)")
	public Object chatRoomMemberUpdateHandle(ProceedingJoinPoint joinPoint, ChatRoomMemberUpdate chatRoomMemberUpdate) throws Throwable {

		ChatRoom room = null;

		if (chatRoomMemberUpdate.value() == ExtractType.BEFORE) {
			room = getChatRoom(joinPoint);
		}

		Object result = joinPoint.proceed();

		if (chatRoomMemberUpdate.value() == ExtractType.AFTER) {
			room = getChatRoom(joinPoint);
		}

		if (room != null) {
			sendChatRoomMemberList(room);
		}

		return result;
	}

	/**
	 * 조인포인트에서 세션 id를 추출한다.
	 * @since 1.0.0
	 */
	private String extractSessionId(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Object[] args = joinPoint.getArgs();

		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			Annotation[] annotations = parameters[i].getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof SessionId) {
					return (String) args[i];
				}
			}
		}

		return null;
	}

	/**
	 * 조인포인트에서 room id를 추출한다.
	 * @since 1.0.0
	 */
	private String extractRoomId(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Object[] args = joinPoint.getArgs();

		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			Annotation[] annotations = parameters[i].getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof RoomId) {
					return (String) args[i];
				}
			}
		}

		return null;
	}

	/**
	 * 조인포인트에서 ChatRoom 을 추출한다.
	 * roomId나 sessionId를 통해서 Chatroom 을 반환한다.
	 * @since 1.0.0
	 */
	private ChatRoom getChatRoom(ProceedingJoinPoint joinPoint) {
		String roomId = extractRoomId(joinPoint);
		if (roomId != null) {
			return getChatRoomByRoomId(roomId);
		}
		String sessionId = extractSessionId(joinPoint);
		if (sessionId != null) {
			return getChatRoomBySessionId(sessionId);
		}
		return null;
	}

	/**
	 * sessionId를 통해 ChatRoom 을 추출한다.
	 * @since 1.0.0
	 */
	private ChatRoom getChatRoomBySessionId(String sessionId) {
		return userService.getChatRoomMap().get(sessionId);
	}

	/**
	 * roomId를 통해 ChatRoom 을 추출한다.
	 * @since 1.0.0
	 */
	private ChatRoom getChatRoomByRoomId(String roomId) {
		return chatRoomService.getRoom(roomId);
	}

	/**
	 * ChatRoom 을 구독하고 있는 세션에 멤버리스트를 전송한다.
	 * @since 1.0.0
	 */
	private void sendChatRoomMemberList(ChatRoom room) {
		String roomId = room.getId();
		List<String> members = room.getMembers();

		messagingTemplate.convertAndSend("/topic/" + roomId + "/members", members);
	}
}

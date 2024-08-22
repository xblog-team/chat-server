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

@Aspect
@Component
@RequiredArgsConstructor
public class ChatRoomMemberListAspect {

	private final UserService userService;
	private final ChatRoomService chatRoomService;
	private final SimpMessagingTemplate messagingTemplate;

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

	private ChatRoom getChatRoomBySessionId(String sessionId) {
		return userService.getChatRoomMap().get(sessionId);
	}

	private ChatRoom getChatRoomByRoomId(String roomId) {
		return chatRoomService.getRoom(roomId);
	}

	private void sendChatRoomMemberList(ChatRoom room) {
		String roomId = room.getId();
		List<String> members = room.getMembers();

		messagingTemplate.convertAndSend("/topic/" + roomId + "/members", members);
	}
}

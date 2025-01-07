package com.xblog.chat.interceptor;

import java.util.List;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import lombok.RequiredArgsConstructor;

/**
 * WEBSOCKET 의 handshake 전 후의 동작을 적용할 인터셉터
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@RequiredArgsConstructor
public class ChatHandshakeInterceptor implements HandshakeInterceptor {

	/**
	 * 핸드셰이크 체결 전 헤더에서 userId와 userRole 을 추출하여 세션 attribute 에 저장한다.
	 *
	 * @since 1.0.0
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
		Map<String, Object> attributes) throws Exception {
		List<String> xUserId = request.getHeaders().get("X-User-ID");
		String userId = xUserId != null && !xUserId.isEmpty() ? xUserId.get(0) : null;
		if (StringUtils.hasText(userId)) {
			attributes.put("userId", userId);
		}

		List<String> xUserRoles = request.getHeaders().get("X-USER-ROLE");
		if (xUserRoles != null && !xUserRoles.isEmpty()) {
			attributes.put("roles", xUserRoles);
		}

		return true;
	}

	/**
	 * 핸드셰이크 체결 후 동작.
	 *
	 * @since 1.0.0
	 */
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
		Exception exception) {

	}
}

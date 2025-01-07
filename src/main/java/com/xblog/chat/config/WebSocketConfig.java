package com.xblog.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.xblog.chat.interceptor.ChatHandshakeInterceptor;

/**
 * 웹소켓 통신을 위한 config
 *
 * @author : 강경훈
 * @version : 1.0.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	/**
	 * 메시지 브로커를 설정한다.
	 * destination prefix 설정
	 *
	 * @since 1.0.0
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic", "/queue");
		registry.setApplicationDestinationPrefixes("/app");
	}

	/**
	 * endpoint 설정 및 CORS 설정
	 * 핸드셰이크 인터셉터 적용
	 *
	 * @since 1.0.0
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/chat")
			.setAllowedOrigins("http://localhost:8080")
			.addInterceptors(new ChatHandshakeInterceptor())
			.withSockJS();
	}
}

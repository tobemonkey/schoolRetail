package edu.hour.schoolretail.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import javax.annotation.Resource;


/**
 * @author demoy
 * @create 10:43
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Resource
	private WebSocketHandler webSocketHandler;

	@Resource
	private HttpSessionIdHandshakeInterceptor httpSessionIdHandshakeInterceptor;

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}


	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketHandler, "/websocket/")
				.addInterceptors(httpSessionIdHandshakeInterceptor)
				.setAllowedOrigins("*");
	}


	/**
	 * websocket 容器
	 * @return
	 */
	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();

		container.setMaxTextMessageBufferSize(512000);
		container.setMaxBinaryMessageBufferSize(512000);
		container.setMaxSessionIdleTimeout(2 * 60 * 1000L);
		return container;
	}
}

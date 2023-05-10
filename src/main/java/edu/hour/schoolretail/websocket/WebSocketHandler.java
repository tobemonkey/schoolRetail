package edu.hour.schoolretail.websocket;

import edu.hour.schoolretail.exception.BusinessException;
import edu.hour.schoolretail.mapper.UserMapper;
import edu.hour.schoolretail.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.WsSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author demoy
 * @create 10:34
 */
@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

	// 用户 id 和 sessionId 的映射
	public static final ConcurrentHashMap<String, Long> sessionIdUserMap = new ConcurrentHashMap<>();

	// session id 和 session 的映射
	public static final ConcurrentHashMap<Long, WebSocketSession> userSessionMap = new ConcurrentHashMap<>();

	/**
	 * 在线状态
	 */
	private static final Integer ONLINE = 1;

	/**
	 * 下线状态
	 */
	private static final Integer OFFLINE = 0;

	@Resource
	private UserMapper userMapper;


	@Override
	public synchronized void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 解析请求路径中的 token 并获取对应的 id
		Long userId = null;
		System.out.println("sessionidusermap ：" + sessionIdUserMap.size());

		try {
			userId = getToken(session);
			if (userId == null) {
				log.error("身份校验失败，请用户重新登入");
				return;
			}
		} catch (Exception e) {
			log.error("ws 获取用户 token 失败，失败信息为：{}", e.getMessage());
			return;
		}

		// 获取对应的 session id
		String httpSessionId = (String) session.getAttributes().get("httpSessionId");
		// 判断是否已经登入
		WebSocketSession oldSession = userSessionMap.getOrDefault(userId, null);
		// 如果有对应的信息说明有登入过，修改信息
		if (oldSession != null) {
			// 如果已经登入且session不一样，则更新map
			String oldSessionId = ((WsSession) ((StandardWebSocketSession) oldSession).getNativeSession()).getHttpSessionId();
			if (!oldSessionId.equals(httpSessionId)) {
				// 删除 oldsession id 会在会话关闭的时候执行，但 userSessionMap 对应的信息只做更新
				userSessionMap.put(userId, session);
				sessionIdUserMap.put(httpSessionId, userId);
				oldSession.close(new CloseStatus(1001, "用户异地登入，请重新登入"));
			}
			// 如果已经登入且和原来的session一致，则do nothing
			else {
				return;
			}
		}
		// 非异地登入且非重复登入，将id和session id作为键值对进行存储，并修改在线状态
		sessionIdUserMap.put(httpSessionId, userId);
		userSessionMap.put(userId, session);
		userMapper.updateOnlineStatus(ONLINE, userId);
	}

	/**
	 * 获取 session 中的 token
	 * @param session
	 * @return
	 */
	private Long getToken(WebSocketSession session) {
		List<String> cookies = session.getHandshakeHeaders().get("cookie");
		String token = null;
		for (String cookie : cookies) {
			int tokenStart = cookie.indexOf("token=") + 6;
			// 如果不存在 token
			if (tokenStart == -1) {
				continue;
			}
			int end1 = cookie.indexOf(";", tokenStart);
			if (end1 == -1) {
				token = cookie.substring(tokenStart);
			} else {
				token = cookie.substring(tokenStart, end1);
			}
			log.info("获取的 token 为：{}", token);
			break;
		}

		try {
			if (token == null) {
				session.sendMessage(new TextMessage("token 不存在"));
			} else {
				try {
					return JWTUtil.getUserIdByToken(token);
				} catch (Exception e) {
					session.sendMessage(new TextMessage("token 解析失败"));
				}
			}
		} catch (IOException e) {
			throw new BusinessException("token 获取失败");
		}
		return null;
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		try {
			if (message.getPayload().equals("ping")) {
				session.sendMessage(new TextMessage("pong"));
			}
		} catch (IllegalStateException e) {
			log.info("用户太久未操作，关闭连接");
		} catch (IOException e) {
			log.error("IO 异常，异常信息为：{}", e.getMessage());
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("关闭连接的会话信息：{}， {}", status.getCode(), status.getReason());
		WsSession closeSession = (WsSession) ((StandardWebSocketSession) session).getNativeSession();
		String sessionId = closeSession.getHttpSessionId();
		Long closeUserId = sessionIdUserMap.remove(sessionId);
		log.info("关闭的用户id：{}", closeUserId);
		// 只有当前会话的id等于请求关闭的session的id时才进行关闭，因为如果多号同时登入，新登入的账号对应的session会关闭旧的session
		if (sessionId.equals(((WsSession) ((StandardWebSocketSession) userSessionMap.get(closeUserId)).getNativeSession()).getHttpSessionId())) {
			userSessionMap.remove(closeUserId);
			userMapper.updateOnlineStatus(OFFLINE, closeUserId);
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.error("引起错误的请求：{}，错误信息为：{}", session.getUri(), exception.getMessage());
		session.close(CloseStatus.SERVER_ERROR);
	}
}

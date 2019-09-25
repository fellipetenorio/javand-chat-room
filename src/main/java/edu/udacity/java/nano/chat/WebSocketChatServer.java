package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint("/chat/{username}")
public class WebSocketChatServer {
    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(String msg, String type, String from) {
        int onlineCount = onlineSessions.size();
        for (Map.Entry<String, Session> entry : onlineSessions.entrySet()) {
            Session session = entry.getValue();
            String username = entry.getKey();

            if (!session.isOpen()) {
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onlineSessions.remove(username);
                continue;
            }

            System.out.println("session::" + entry.getKey() + "::sessions: " + session.getOpenSessions().size());
            try {
//                for (Session sess : session.getOpenSessions()) {
//                    if (sess.isOpen()) {
                session.getBasicRemote().sendText(Message.jsonConverter(type, from, msg, onlineCount));
//                    }
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getUsername(Session sess) {
        return sess.getRequestParameterMap().get("username").get(0);
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen::" + session.getId());
        final String username = getUsername(session);
        onlineSessions.put(username, session);
        sendMessageToAll("entered", Message.ENTER, username);
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        System.out.println("onMessage: " + jsonStr);
        Message msgEnvelope = JSON.parseObject(jsonStr, Message.class);
        String msgType = msgEnvelope.getType();
        String type = Message.SPEAK;
        String msg = msgEnvelope.getMsg();

        if (null == msgType || "".equals(msgType)) {
            type = Message.SPEAK;
        } else if(Message.SPEAK.equals(msgType)) {
            type = Message.SPEAK;
        } else if (Message.QUIT.equals(msgType)) {
            msg = "leaved";
        } else if(Message.ENTER.equals(msgType)) {
            type = Message.ENTER;
            msg = "Entered";
        }

        sendMessageToAll(msg, type, msgEnvelope.getUsername());
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        String username = getUsername(session);
        onlineSessions.remove(username);
        sendMessageToAll("leaved", Message.SPEAK, username);
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}

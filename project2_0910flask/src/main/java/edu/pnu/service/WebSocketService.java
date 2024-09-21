package edu.pnu.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;



import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class WebSocketService {

    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new CopyOnWriteArraySet<>());

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public void sendMessage(String jsonData) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(jsonData)); // JSON 변환 필요
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

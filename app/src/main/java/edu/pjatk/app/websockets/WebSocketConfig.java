package edu.pjatk.app.websockets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final static String CHAT_ENDPOINT = "/conversation";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {  // TODO podmienic * na adres serwera
        registry.addEndpoint("/conversation").setAllowedOrigins("*").withSockJS();  //TODO CHAT_ENDPOINT
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setApplicationDestinationPrefixes("/app").enableSimpleBroker("/topic");
        registry.enableSimpleBroker("/topic");

    }
}

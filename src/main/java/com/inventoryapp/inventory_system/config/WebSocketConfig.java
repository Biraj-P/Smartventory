package com.inventoryapp.inventory_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //Enables WebSocket message handling, backed by a message broker.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 1.Broker Prefix: Defines the destination for messages broadcasted from the server to clients.
        config.enableSimpleBroker("/topic");

        // 2.Application Destination Prefix: Defines the destination for messages sent from the client to the server.
        //Controlers will map endpoints under this prefix to handle messages.
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //Defines the WebSocket endpoint URL that clients will use to connect to the WebSocket server.
        // .withSockJS() adds fallback options for browsers that don’t support WebSockets.
        registry.addEndpoint("/ws-inventory").withSockJS();
    }
}

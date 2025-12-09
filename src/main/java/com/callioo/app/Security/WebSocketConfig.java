package com.callioo.app.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.callioo.app.Controller.UserWSHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        System.out.println("Inside register WebSocketHandlers");

        registry.addHandler(new UserWSHandler(), "/ws")
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOrigins(SecurityConfig.FRONTEND_URL);
    }

}

package com.auto.test.config;

import com.auto.test.service.common.PodService;
import com.auto.test.websocket.K8sPodLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
  
  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }
  @Autowired
  public void setSenderService(PodService senderService) {
    K8sPodLogService.setPodService(senderService);
  }
}

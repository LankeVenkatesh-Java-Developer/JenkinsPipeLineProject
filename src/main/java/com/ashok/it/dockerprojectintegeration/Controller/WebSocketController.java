package com.ashok.it.dockerprojectintegeration.Controller;

import com.ashok.it.dockerprojectintegeration.Model.Product;
import com.ashok.it.dockerprojectintegeration.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final ProductService productService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/product")
    @SendTo("/topic/products")
    public Product broadcastProduct(Product product) {
        return product;
    }

    public void broadcastCreate(Product product) {
        messagingTemplate.convertAndSend("/topic/products", 
            new ProductNotification("CREATE", product));
    }

    public void broadcastUpdate(Product product) {
        messagingTemplate.convertAndSend("/topic/products", 
            new ProductNotification("UPDATE", product));
    }

    public void broadcastDelete(Long id) {
        messagingTemplate.convertAndSend("/topic/products", 
            new ProductNotification("DELETE", id));
    }

    record ProductNotification(String action, Object data) {}
}

package com.example.Microservices.events;

import com.example.Microservices.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class SendProductDetailsResponse {

    @Autowired
    private JmsTemplate jms;

    public void SendProductDetails(Product productDetails) {

        jms.convertAndSend("Product-details-response", productDetails.getId());
        jms.convertAndSend("Product-details-response", productDetails.getName());
        jms.convertAndSend("Product-details-response", productDetails.getDescription());
        jms.convertAndSend("Product-details-response", productDetails.getPrice());
        jms.convertAndSend("Product-details-response", productDetails.getQuantityAvailable());
        System.out.println("Product details sent successfully");
    }

    public void SendProductDetailsbyName(Product productDetails) {

        jms.convertAndSend("Product-details-name-response", productDetails.getId());
        jms.convertAndSend("Product-details-name-response", productDetails.getName());
        jms.convertAndSend("Product-details-name-response", productDetails.getDescription());
        jms.convertAndSend("Product-details-name-response", productDetails.getPrice());
        jms.convertAndSend("Product-details-name-response", productDetails.getQuantityAvailable());
        System.out.println("Product details sent successfully");
    }
}

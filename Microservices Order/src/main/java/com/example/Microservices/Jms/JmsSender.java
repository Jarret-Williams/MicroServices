package com.example.Microservices.Jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsSender {

    @Autowired
    private JmsTemplate jms;

    public void askProductDetails(Long Productid) {
        jms.convertAndSend("Product-details-request", Productid);
    }
    public void askDecreaseProductQuantity(Long Productid, int quantity) {
        jms.convertAndSend("Product-QuantityDecrease-request", Productid + "," + quantity);
    }

    public void askProductDetailsByName(String ProductName){
        jms.convertAndSend("Product-details-name-request", ProductName);
    }

}

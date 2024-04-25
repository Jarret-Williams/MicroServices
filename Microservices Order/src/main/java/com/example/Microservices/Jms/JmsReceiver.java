package com.example.Microservices.Jms;

import com.example.Microservices.RestClientDto.ProductDto;
import com.example.Microservices.services.OrderService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JmsReceiver {

    private final OrderService orderService;
    private List<String> list = new ArrayList<>();

    public JmsReceiver(OrderService orderService) {
        this.orderService = orderService;
    }

    @JmsListener(destination = "Product-details-response")
    public void receiveMessage(String message){
        //System.out.println("The message was " + message);
        list.add(message);
        if (list.size() >= 5){
            ProductDto prod = createProduct(list);
            list.clear();
            orderService.setProdDetails(prod);
        }
    }

    @JmsListener(destination = "Product-details-name-response")
    public void receiveProductDetailsByName(String message){
        //System.out.println("The message was " + message);
        list.add(message);
        if (list.size() >= 5){
            ProductDto prod = createProduct(list);
            list.clear();
            orderService.setProdDetails(prod);
        }
    }
    public ProductDto createProduct(List<String> msg){
        ProductDto product = new ProductDto();
        product.setId(Long.valueOf(msg.get(0)));
        product.setName(list.get(1));
        product.setDescription(list.get(2));
        product.setPrice(Double.valueOf(list.get(3)));
        product.setQuantityAvailable(Integer.valueOf(list.get(4)));
        return product;
    }

}

package com.example.Microservices.services;


import com.example.Microservices.exceptions.NOT_FOUND.NotFoundRequestException;
import com.example.Microservices.models.OrderItem;
import com.example.Microservices.repository.OrderItemListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    public final OrderItemListRepository orderItemListRepository;

    public OrderItemService(OrderItemListRepository orderItemListRepository) {
        this.orderItemListRepository = orderItemListRepository;
    }
    public List<OrderItem> findAllOrderItems(){
        return orderItemListRepository.findAll();
    }
    public List<OrderItem> findOrdersWithId (long orderId){
        List<OrderItem> exists = orderItemListRepository.findOrdersWithId(orderId);
        if (exists.isEmpty()){
            throw new NotFoundRequestException("OrderItems with id " + orderId + " not found");
        }
        return orderItemListRepository.findOrdersWithId(orderId);
    }
}

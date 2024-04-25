package com.example.Microservices.controllers;


import com.example.Microservices.models.Order;
import com.example.Microservices.models.OrderItem;
import com.example.Microservices.services.OrderItemService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders/items")
@Hidden
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public List<OrderItem> getAllOrders (Order order) {
        List<OrderItem> allOrders = orderItemService.findAllOrderItems();
        return allOrders;
    }

    @GetMapping(path = "{orderId}")
    public List<OrderItem> getOrderById(@PathVariable("orderId") Long orderId){
        return orderItemService.findOrdersWithId(orderId);
    }
}

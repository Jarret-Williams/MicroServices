package com.example.Microservices.events;

import com.example.Microservices.models.Order;
import com.example.Microservices.models.Status;
import com.example.Microservices.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateOrderStatusEvent {

    private final OrderRepository orderRepository;

    public UpdateOrderStatusEvent(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalStateException("Order not found"));
        if (status.equals("CREATED")) {
            order.setStatus(Status.CREATED);
        }
        if (status.equals("SHIPPED")) {
            order.setStatus(Status.SHIPPED);
        }
        if (status.equals("CLOSED")) {
            order.setStatus(Status.CLOSED);
        }
        if (status.equals("CANCELLED")) {
            order.setStatus(Status.CANCELLED);
        }
        orderRepository.save(order);
    }
}

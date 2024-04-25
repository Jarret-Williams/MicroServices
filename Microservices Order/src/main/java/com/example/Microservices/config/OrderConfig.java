package com.example.Microservices.config;

import com.example.Microservices.models.Order;
import com.example.Microservices.models.OrderItem;
import com.example.Microservices.models.Status;
import com.example.Microservices.repository.OrderItemListRepository;
import com.example.Microservices.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OrderConfig {

    private final OrderItemListRepository orderItemListRepository;


    public OrderConfig(OrderItemListRepository orderItemListRepository) {
        this.orderItemListRepository = orderItemListRepository;

    }

    @Bean
    CommandLineRunner commandLineRunnerOrder(OrderRepository orderRepository){
        List<OrderItem> list = new ArrayList<>();
        list.add(new OrderItem(
                1,
                1,
                2
        ));
//        list.add(new OrderItem(
//                1,
//                3,
//                3
//        ));
        list.add(new OrderItem(
                1,
                2,
                4
        ));
        List<OrderItem> list2 = new ArrayList<>();
        list.add(new OrderItem(
                2,
                4,
                2
        ));
//        list.add(new OrderItem(
//                2,
//                3,
//                4
//        ));
        list.add(new OrderItem(
                2,
                5,
                3
        ));
        return args -> {
            Order order1 = new Order(
                    list,
                    0,
                    Status.CREATED
            );
            Order order2 = new Order(
                    list2,
                    0,
                    Status.CREATED
            );
            orderRepository.saveAll(List.of(order1,order2));
            list.addAll(list2);
            orderItemListRepository.saveAll(list);
        };
    }
}

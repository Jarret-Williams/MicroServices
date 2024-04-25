package com.example.Microservices.repository;

import com.example.Microservices.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemListRepository extends JpaRepository<OrderItem,Long> {


    @Query("SELECT i FROM OrderItem i WHERE i.orderId =?1")
    List<OrderItem> findOrdersWithId(Long orderId);

}

package com.example.Microservices.controllers;

import com.example.Microservices.RestClientDto.ClientOrder;
import com.example.Microservices.events.UpdateOrderStatusEvent;
import com.example.Microservices.models.OrderDetails;
import com.example.Microservices.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")

public class OrderController {


    private final OrderService orderService;
    private final UpdateOrderStatusEvent updateOrderStatus;


    @Autowired
    public OrderController(OrderService orderService, UpdateOrderStatusEvent updateOrderStatus) {
        this.orderService = orderService;
        this.updateOrderStatus = updateOrderStatus;
    }

    @GetMapping
    public List<ClientOrder> getAllOrdersWithNewFormat() throws InterruptedException {
        return orderService.findAllWithNewFormat();
    }


    @GetMapping(path = "event/update/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable("id") Long id,
                                               @RequestParam String status){
        updateOrderStatus.updateOrderStatus(id,status);

        return ResponseEntity.ok(status);
    }

    @PostMapping("/create")
    public ResponseEntity createOrder(@RequestBody List<OrderDetails> bod) throws InterruptedException {
        ClientOrder order = orderService.createOrderProcess(bod);
        return new ResponseEntity(order,HttpStatus.CREATED);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    public Page<ClientOrder> getOrdersWithPagination(
            @PathVariable int offset,
            @PathVariable int pageSize) throws InterruptedException {
        Page<ClientOrder> ordersWithPagination = orderService.findAllWithItemsPaginationNewFormat(offset, pageSize);
        return ordersWithPagination;
    }
    @GetMapping(path = "{orderId}")
    public ClientOrder getOrderById(@PathVariable("orderId") Long orderId) throws InterruptedException {
        return orderService.findOrderWithId(orderId);
    }
    //    @PostMapping("/create")
//    public void createOrder(@RequestBody Order order) throws InterruptedException {
//        System.out.println(orderService.getLastId());
//        orderService.createNewOrder(order);
//        orderService.calcTotal(order.getId());
//
//    }
    //    @GetMapping("/pagination/{offset}/{pageSize}")
//    public Page<Order> getOrdersWithPagination(
//            @PathVariable int offset,
//            @PathVariable int pageSize)
//    {
//        Page<Order> ordersWithPagination = orderService.findAllWithItemsPagination(offset, pageSize);
//        return ordersWithPagination;
//    }
    //    @GetMapping
//    public List<Order> getAllOrders (){
//        List<Order> allOrders = orderService.findAllWithItems();
//        return allOrders;
//    }
}

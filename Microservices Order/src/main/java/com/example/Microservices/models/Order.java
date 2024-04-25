package com.example.Microservices.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "Orders")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    private long orderId;
    @Transient
    private List<OrderItem> items = new ArrayList<>();
    private double total;
    @Enumerated(EnumType.STRING)
    private Status status;

    public long getId() {
        return orderId;
    }
    public void setId(long OrderId) {
        this.orderId = OrderId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Order(List<OrderItem> items, double total, Status status) {
        this.items = items;
        this.total = total;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "OrderId=" + orderId +
                ", items=" + items +
                ", total=" + total +
                ", status=" + status +
                '}';
    }
}

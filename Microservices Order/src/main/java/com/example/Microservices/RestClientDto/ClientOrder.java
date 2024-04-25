package com.example.Microservices.RestClientDto;

import com.example.Microservices.models.Status;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class ClientOrder {

    private List<ClientListItems> items;
    private double total;
    private Status status;
    private long id;

    public List<ClientListItems> getItems() {
        return items;
    }

    public void setItems(List<ClientListItems> items) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ClientOrder(List<ClientListItems> items, double total, Status status, long id) {
        this.items = items;
        this.total = total;
        this.status = status;
        this.id = id;
    }
}

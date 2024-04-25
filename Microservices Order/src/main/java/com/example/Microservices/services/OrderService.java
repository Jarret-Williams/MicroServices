package com.example.Microservices.services;

import com.example.Microservices.Jms.JmsSender;
import com.example.Microservices.RestClientDto.ClientListItems;
import com.example.Microservices.RestClientDto.ClientOrder;
import com.example.Microservices.RestClientDto.ProductDto;
import com.example.Microservices.exceptions.BAD_REQ.QuantityRequestException;
import com.example.Microservices.exceptions.NOT_FOUND.NotFoundRequestException;
import com.example.Microservices.models.Order;
import com.example.Microservices.models.OrderDetails;
import com.example.Microservices.models.OrderItem;
import com.example.Microservices.models.Status;
import com.example.Microservices.repository.OrderItemListRepository;
import com.example.Microservices.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemListRepository orderItemListRepository;
    private final OrderItemService orderItemService;
    private final JmsSender jmsSender;
    private ProductDto productObject;
    private Order globalOrder;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemListRepository orderItemListRepository, OrderItemService orderItemService, JmsSender jmsSender) {
        this.orderRepository = orderRepository;
        this.orderItemListRepository = orderItemListRepository;
        this.orderItemService = orderItemService;
        this.jmsSender = jmsSender;
    }

    public List<Order> findAllWithItems() {
        List<Order> newList = new ArrayList<>();
        List<Order> all = orderRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            Order item = all.get(i);
            Long id = item.getId();

            List<OrderItem> orderItems = orderItemListRepository.findOrdersWithId(id);
            item.setItems(orderItems);
            newList.add(item);
        }
        return newList;
    }
    public Page<Order> findAllWithItemsPagination(int offset, int pageSize) {
        Pageable pageable = PageRequest.of(offset, pageSize);
//        List<Order> newList = new ArrayList<>();
//        List<Order> all = orderRepository.findAll();
//        for (int i = 0; i < all.size(); i++) {
//            Order item = all.get(i);
//            Long id = item.getId();
//
//            List<OrderItem> orderItems = orderItemListRepository.findOrdersWithId(id);
//            item.setItems(orderItems);
//            newList.add(item);
//        }
        findAllWithItems();
        Page<Order> pagesOrder = orderRepository.findAll(pageable);
        return pagesOrder;
    }
    public List<ClientOrder> findAllWithNewFormat() throws InterruptedException {
        List<ClientOrder> clientOrdersList = new ArrayList<>();
        List<Order> allOrders = orderRepository.findAll();
        for (int i = 0; i < allOrders.size(); i++) {
            List<ClientListItems> clientItemsLists = new ArrayList<>();
            Order order = allOrders.get(i);
            Long id = order.getId();
            List<OrderItem> orderItems = orderItemListRepository.findOrdersWithId(id);

            for (int j = 0; j < orderItems.size(); j++) {
                OrderItem orderItem  = orderItems.get(j);
                int productId = orderItem.getProductId();
                ProductDto productDto = findProductDetails(Long.valueOf(productId));
                if (productDto.getName() == null){
                    throw new NotFoundRequestException("Product not found");
                }
                int quantity = orderItem.getQuantity();

                ClientListItems clientItem = new ClientListItems();
                clientItem.setProductDetails(productDto);
                clientItem.setQuanitity(quantity);
                clientItemsLists.add(clientItem);
            }

            ClientOrder clientOrder = new ClientOrder();
            clientOrder.setId(order.getId());
            clientOrder.setItems(clientItemsLists);
            clientOrder.setTotal(order.getTotal());
            clientOrder.setStatus(order.getStatus());

            clientOrdersList.add(clientOrder);
        }
        return clientOrdersList;
    }

    public Page<ClientOrder> findAllWithItemsPaginationNewFormat(int offset, int pageSize) throws InterruptedException {
        Pageable pageable = PageRequest.of(offset, pageSize);

        int start = offset;
        int end = Math.min((start + pageSize), findAllWithNewFormat().size());
        List<ClientOrder> content = findAllWithNewFormat().subList(start,end);
        return new PageImpl<>(content,pageable,findAllWithNewFormat().size());
    }

    public ClientOrder findOrderWithId(long orderId) throws InterruptedException {
        boolean exists = orderRepository.existsById(orderId);
        if (!exists) {
            throw new NotFoundRequestException("Order with id " + orderId + " not found");
        }
        List<ClientListItems> clientItemsLists = new ArrayList<>();
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new NotFoundRequestException("Order not found"));
        List<OrderItem> orderItems = orderItemListRepository.findOrdersWithId(orderId);

        for (int j = 0; j < orderItems.size(); j++) {
            OrderItem orderItem  = orderItems.get(j);
            int productId = orderItem.getProductId();
            ProductDto productDto = findProductDetails(Long.valueOf(productId));
            if (productDto.getName() == null){
                throw new NotFoundRequestException("Product not found");
            }
            int quantity = orderItem.getQuantity();

            ClientListItems clientItem = new ClientListItems();
            clientItem.setProductDetails(productDto);
            clientItem.setQuanitity(quantity);
            clientItemsLists.add(clientItem);
        }

        ClientOrder clientOrder = new ClientOrder();
        clientOrder.setId(order.getId());
        clientOrder.setItems(clientItemsLists);
        clientOrder.setTotal(order.getTotal());
        clientOrder.setStatus(order.getStatus());


        return clientOrder;
    }

    public void createNewOrder(Order order) throws InterruptedException {
        decreaseQuantityOfProduct(order);
        List<OrderItem> list = orderItemListRepository.findAll();
        list.addAll(order.getItems());
        orderItemListRepository.saveAll(list);
        orderRepository.save(order);
    }

    public void decreaseQuantityOfProduct(Order order) throws InterruptedException {
        List<OrderItem> list = order.getItems();
        for (int i = 0; i < list.size(); i++) {
            OrderItem o1 = list.get(i);
            long productId = o1.getProductId();
            int orderquantity = o1.getQuantity();

            ProductDto currentProduct= findProductDetails(productId);
            if (currentProduct.getName() == null){
                throw new NotFoundRequestException("Product not found");
            }

            if (orderquantity > currentProduct.getQuantityAvailable()) {
                throw new QuantityRequestException("Not enough product in stock");
            } else {
                jmsSender.askDecreaseProductQuantity(productId,orderquantity);
            }
        }
    }

    public void calcTotal(Long id) throws InterruptedException {
        double total = 0;
        List<OrderItem> list = orderItemService.findOrdersWithId(id);
        for (int i = 0; i < list.size(); i++) {
            OrderItem o1 = list.get(i);
            long productId = o1.getProductId();
            int quantity = o1.getQuantity();

            ProductDto currentProduct = findProductDetails(productId);
            if (currentProduct.getName() == null){
                throw new NotFoundRequestException("Product not found");
            }

            if (quantity > currentProduct.getQuantityAvailable()){
                throw new QuantityRequestException("Not enough product in stock");
            }
            double price = currentProduct.getPrice();
            total = total + (price*quantity);
        }
        Order currentOrder = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundRequestException("Order not found"));

        currentOrder.setTotal(total);
        orderRepository.save(currentOrder);
        globalOrder = currentOrder;
    }

    public ProductDto findProductDetails (Long id) throws InterruptedException {
        productObject = new ProductDto();
        jmsSender.askProductDetails(id);
        Thread.sleep(70);
        return productObject;

    }
    public void setProdDetails(ProductDto product){
        productObject = product;

    }

    public ProductDto findProductDetailsByName(String ProductName) throws InterruptedException{
        productObject = new ProductDto();
        jmsSender.askProductDetailsByName(ProductName);
        Thread.sleep(70);
        return productObject;
    }
    public Long getLastId(){
        List<Order> list = orderRepository.findAll();
        Order lastOrder = list.get(list.size()-1);
        Long lastId = lastOrder.getId();
        return lastId;
    }
    public List<OrderItem> convertToOrderItem(List<OrderDetails> trimList, long id) throws InterruptedException {
        List<OrderItem> newList = new ArrayList<>();

        for (int i = 0; i < trimList.size(); i++) {
            ProductDto product = new ProductDto();
            OrderDetails clientList = trimList.get(i);
            OrderItem newItemList = new OrderItem();

            product = findProductDetails(clientList.getProductId());
            Thread.sleep(150);

            if (product.getName() == null){
                throw new NotFoundRequestException("Product not found");
            }

            newItemList.setOrderId(id);
            newItemList.setProductId(Math.toIntExact(product.getId()));
            newItemList.setQuantity(clientList.getQuantity());
            newList.add(newItemList);
        }
        return newList;
    }

    public ClientOrder createOrderProcess(List<OrderDetails> bod) throws InterruptedException {
        long lastId = getLastId()+1;
        List<OrderItem> newList = convertToOrderItem(bod, lastId);

        Order newOrder = new Order(lastId,newList,0, Status.CREATED);
        createNewOrder(newOrder);
        calcTotal(newOrder.getId());

        globalOrder.setItems(newList);

        List<ClientListItems> clientItemsLists = new ArrayList<>();
        Order order = newOrder;
        Long id = order.getId();
        List<OrderItem> orderItems = orderItemListRepository.findOrdersWithId(id);

        for (int j = 0; j < orderItems.size(); j++) {
            OrderItem orderItem  = orderItems.get(j);
            int productId = orderItem.getProductId();
            ProductDto productDto = findProductDetails(Long.valueOf(productId));
            if (productDto.getName() == null){
                throw new NotFoundRequestException("Product not found");
            }
            int quantity = orderItem.getQuantity();

            ClientListItems clientItem = new ClientListItems();
            clientItem.setProductDetails(productDto);
            clientItem.setQuanitity(quantity);
            clientItemsLists.add(clientItem);
        }

        ClientOrder clientOrder = new ClientOrder();
        clientOrder.setId(order.getId());
        clientOrder.setItems(clientItemsLists);
        clientOrder.setTotal(globalOrder.getTotal());
        clientOrder.setStatus(order.getStatus());


        return clientOrder;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void setPresetTotals() throws InterruptedException {
        findProductDetails(1L);
        calcTotal(1L);
        calcTotal(2L);
        jmsSender.askProductDetailsByName("Tea");
        jmsSender.askDecreaseProductQuantity(1L,0);
    }
}

package com.example.Microservices.events;

import com.example.Microservices.models.Product;
import com.example.Microservices.repository.ProductRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class DecreaseProductQuantityEvent {


    private final ProductRepository productRepository;

    public DecreaseProductQuantityEvent(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @JmsListener(destination = "Product-QuantityDecrease-request")
    public void productDetailRequest(String message){
        System.out.println("Product decrease Requested");
        String[] array = message.split(",");
        long productId = Long.parseLong(array[0]);
        int quantity = Integer.parseInt(array[1]);
        decreaseProductQuantity(productId,quantity);
    }

    public void decreaseProductQuantity(long productId, int quantity){
        Product currentProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product not found") );
        currentProduct.setQuantityAvailable(currentProduct.getQuantityAvailable()-quantity);
        productRepository.save(currentProduct);
        System.out.println("Product quantity decreased for product: " + productId);
    }
}

package com.example.Microservices.events;

import com.example.Microservices.models.Product;
import com.example.Microservices.services.ProductService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailsRequest {

    private final ProductService productService;
    private final SendProductDetailsResponse sendProductDetailsResponse;

    public ProductDetailsRequest(ProductService productService, SendProductDetailsResponse sendProductDetailsResponse) {
        this.productService = productService;
        this.sendProductDetailsResponse = sendProductDetailsResponse;
    }

    @JmsListener(destination = "Product-details-request")
    public void productDetailRequest(Long ProductId){

        System.out.println("Product details of product: " + ProductId + " Requested");
        Product product = productService.findProductDetailsById(ProductId)
                .orElseThrow(() -> new IllegalStateException("Product not found"));
        sendProductDetailsResponse.SendProductDetails(product);
    }

    @JmsListener(destination = "Product-details-name-request")
    public void productDetailNameRequest(String ProductName){

        System.out.println("Product-details-name-request: " + ProductName + " Requested");
        Product product = productService.findProductDetailsByName(ProductName)
                .orElseThrow(() -> new IllegalStateException("Product not found"));
        sendProductDetailsResponse.SendProductDetailsbyName(product);
    }
}

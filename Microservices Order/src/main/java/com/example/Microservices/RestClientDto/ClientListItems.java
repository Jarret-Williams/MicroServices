package com.example.Microservices.RestClientDto;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class ClientListItems {

    private ProductDto productDetails;
    private int quanitity;

    public ProductDto getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDto productDetails) {
        this.productDetails = productDetails;
    }

    public int getQuanitity() {
        return quanitity;
    }

    public void setQuanitity(int quanitity) {
        this.quanitity = quanitity;
    }

    public ClientListItems(ProductDto productDetails, int quanitity) {
        this.productDetails = productDetails;
        this.quanitity = quanitity;
    }
}

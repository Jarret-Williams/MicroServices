package com.example.Microservices.controllers;

import com.example.Microservices.models.Product;
import com.example.Microservices.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getProducts(){
        List<Product> allProducts = productService.findAllProducts();
        return allProducts;
    }
    @GetMapping(path = "{productId}")
    public Optional<Product> getProductById(@PathVariable("productId") Long productId){
        return productService.findProductDetailsById(productId);
    }
    @GetMapping("/pagination/{offset}/{pageSize}")
    public Page<Product> getProductsWithPagination(
            @PathVariable int offset,
            @PathVariable int pageSize)
    {
        Page<Product> productsWithPagination = productService.findProductsWithPagination(offset, pageSize);
        return productsWithPagination;
    }

//    @PostMapping(path = "/create")
//    public void createProduct(@RequestBody Product product){
//        productService.createNewProduct(product);
//    }
    @PostMapping(path = "/create")
    public void createProduct(@RequestParam String name,
                              @RequestParam String description,
                              @RequestParam double price,
                              @RequestParam Integer quantity){
        Product newProd = new Product();
        newProd.setName(name);
        newProd.setDescription(description);
        newProd.setPrice(price);
        newProd.setQuantityAvailable(quantity);

        productService.createNewProduct(newProd);
    }

    @PutMapping(path = "/update")
    public void updateProductDetails(@RequestBody Product product){
        productService.updateProductDetails(product);
    }
}

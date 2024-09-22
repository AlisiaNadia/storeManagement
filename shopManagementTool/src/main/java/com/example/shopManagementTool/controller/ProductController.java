package com.example.shopManagementTool.controller;

import com.example.shopManagementTool.entity.Product;
import com.example.shopManagementTool.inputData.ProductDO;
import com.example.shopManagementTool.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/addProduct")
    public ResponseEntity<Product> saveProduct(@RequestBody ProductDO productDO) {
        return productService.validateAndSaveProduct(productDO);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<ProductDO>> getAllProducts() {
        return productService.getAllProducts();
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

}

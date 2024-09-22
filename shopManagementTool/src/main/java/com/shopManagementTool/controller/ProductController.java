package com.shopManagementTool.controller;

import com.shopManagementTool.entity.Product;
import com.shopManagementTool.inputData.ProductDO;
import com.shopManagementTool.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PutMapping("/updateProductQuantity/{id}/{quantity}")
    public ResponseEntity<Product> updateProductQuantity(@PathVariable Long id, @PathVariable Integer quantity) {
        return productService.validateAndUpdateProduct(id, quantity);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

}

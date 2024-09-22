package com.shopManagementTool.controller;

import com.shopManagementTool.inputData.ProductDO;
import com.shopManagementTool.inputData.ResponseDO;
import com.shopManagementTool.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class ProductController {

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final String INVALID_DATA = "invalid data";
    private static final String NOT_FOUND = "not found";

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/addProduct")
    public ResponseEntity<ProductDO> saveProduct(@RequestBody ProductDO productDO) {

        ResponseDO responseDO = productService.validateAndSaveProduct(productDO);
        return prepareResponseEntity(responseDO);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<ProductDO>> getAllProducts() {
        List<ProductDO> productDOList = productService.getAllProducts();

        if (productDOList == null ) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return  new ResponseEntity<>(productDOList, HttpStatus.OK);
    }

    @PutMapping("/updateProductQuantity/{id}/{quantity}")
    public ResponseEntity<ProductDO> updateProductQuantity(@PathVariable Long id, @PathVariable Integer quantity) {
        ResponseDO responseDO = productService.validateAndUpdateProductQuantity(id, quantity);
        return prepareResponseEntity(responseDO);
    }

    @PutMapping("/updateProductPrice/{id}/{price}")
    public ResponseEntity<ProductDO> updateProductPrice(@PathVariable Long id, @PathVariable Double price) {
        ResponseDO responseDO = productService.validateAndUpdateProductPrice(id, price);
        return prepareResponseEntity(responseDO);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<ProductDO> deleteProduct(@PathVariable Long id) {
        ResponseDO responseDO = productService.deleteProduct(id);
        return prepareResponseEntity(responseDO);
    }

    private ResponseEntity<ProductDO> prepareResponseEntity(ResponseDO responseDO) {

        return switch (responseDO.getStatus()) {
            case SUCCESS -> new ResponseEntity<>(responseDO.getProductDO(), HttpStatus.OK);
            case ERROR -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            case INVALID_DATA -> new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            case NOT_FOUND -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            default -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        };
    }
}

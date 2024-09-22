package com.example.shopManagementTool.service;

import com.example.shopManagementTool.convert.ProductConverter;
import com.example.shopManagementTool.entity.Product;
import com.example.shopManagementTool.entity.repository.ProductRepository;
import com.example.shopManagementTool.inputData.ProductDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductService {
    Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductConverter productConverter) {
        this.productRepository = productRepository;
        this.productConverter = productConverter;
    }

    public ResponseEntity<Product> validateAndSaveProduct(ProductDO productDO) {
        logger.atDebug().log("validateAndSaveProduct in ProductService.");

        if (containsIllegalsCharacters(productDO.getName())
                && containsIllegalsCharacters(productDO.getDescription())
                && (Double.isNaN(productDO.getPrice()) || productDO.getPrice() <= 0)
                && productDO.getQuantity() <= 0) {

            logger.atDebug().log("The new product to be saved contains invalid data.");

            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);

        } else {
            if (saveProduct(productDO) != null) {
                return ResponseEntity.ok(saveProduct(productDO));
            }
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public Product saveProduct(ProductDO productDO) {
        logger.atDebug().log("checkAndSaveProduct in ProductService.");

        try {
            Product product = productConverter.convertProductDOToBE(productDO);
            logger.atInfo().log("Save new product");
            return productRepository.save(product);
        } catch (Exception e) {
            logger.atError().addArgument(e).log("Unable to save a new product. Reason: {}");
            return null;
        }
    }

    public ResponseEntity<List<ProductDO>> getAllProducts() {
        logger.atInfo().log("Get all products.");
        List<ProductDO> productDOList = new ArrayList<>();
        try {
            List<Product> productList = productRepository.findAll();
            productList.forEach(product -> productDOList.add(productConverter.convertProductBEToDO(product)));
        } catch (Exception e) {
            logger.atError().addArgument(e).log("Unable to get the product list. Reason: {}");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(productDOList, HttpStatus.OK);
    }

    public Product updateProduct(Product updatedProduct) {
        return updatedProduct;
    }

    public ResponseEntity<Product> deleteProduct(Long id) {
        logger.atInfo().log("Delete the product.");
        try {
            if (productRepository.existsById(id)) {
                logger.atDebug().addArgument(id).log("The product with id {} exist and can be deleted.");
                productRepository.deleteById(id);
            }
        } catch (Exception e) {
            logger.atError().addArgument(e).log("Unable to delete the product. Reason: {}");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    public boolean containsIllegalsCharacters(String toCheck) {
        Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\_^-]");
        Matcher matcher = pattern.matcher(toCheck);
        return matcher.find();
    }
}

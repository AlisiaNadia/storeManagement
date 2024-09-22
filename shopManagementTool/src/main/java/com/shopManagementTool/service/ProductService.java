package com.shopManagementTool.service;

import com.shopManagementTool.converter.ProductConverter;
import com.shopManagementTool.entity.Product;
import com.shopManagementTool.entity.repository.ProductRepository;
import com.shopManagementTool.inputData.ProductDO;
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

        if (validateProduct(productDO)) {
            logger.atDebug().log("The new product to be saved contains invalid data.");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);

        } else {
            Product product = saveProduct(productDO);
            if (product != null) {
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

    public ResponseEntity<Product> validateAndUpdateProductQuantity(Long id, Integer quantity) {

        if (quantity < 0 && id == null) {
            logger.atDebug().log("The product to be update contains invalid data.");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        } else {
            try {
                Product product = productRepository.findById(id).map(productToBeUpdated -> {
                    productToBeUpdated.setQuantity(quantity);
                    return productRepository.save(productToBeUpdated);
                }).orElse(null);

                if (product != null) {
                    return ResponseEntity.ok(saveProduct(productConverter.convertProductBEToDO(product)));
                }
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                logger.atError().addArgument(e).log("Unable to update the product quantity. Reason: {}");
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public ResponseEntity<Product> validateAndUpdateProductPrice(Long id, Double price) {

        if ((Double.isNaN(price) || price <= 0) && id == null) {
            logger.atDebug().log("The product to be update contains invalid data.");
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        } else {
            try {
                Product product = productRepository.findById(id).map(productToBeUpdated -> {
                    productToBeUpdated.setPrice(price);
                    return productRepository.save(productToBeUpdated);
                }).orElse(null);

                if (product != null) {
                    return ResponseEntity.ok(saveProduct(productConverter.convertProductBEToDO(product)));
                }
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                logger.atError().addArgument(e).log("Unable to update the product price. Reason: {}");
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
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

    public boolean validateProduct(ProductDO productDO) {
        return (containsIllegalsCharacters(productDO.getName())
                && containsIllegalsCharacters(productDO.getDescription())
                && (Double.isNaN(productDO.getPrice()) || productDO.getPrice() <= 0)
                && productDO.getQuantity() < 0);
    }

    public boolean containsIllegalsCharacters(String toCheck) {
        Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\_^-]");
        Matcher matcher = pattern.matcher(toCheck);
        return matcher.find();
    }
}

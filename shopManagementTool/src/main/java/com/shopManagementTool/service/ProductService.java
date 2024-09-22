package com.shopManagementTool.service;

import com.shopManagementTool.converter.ProductConverter;
import com.shopManagementTool.entity.Product;
import com.shopManagementTool.entity.repository.ProductRepository;
import com.shopManagementTool.inputData.ProductDO;
import com.shopManagementTool.inputData.ResponseDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductService {

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final String INVALID_DATA = "invalid data";
    private static final String NOT_FOUND = "not found";

    Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductConverter productConverter) {
        this.productRepository = productRepository;
        this.productConverter = productConverter;
    }

    /**
     * Method that validates if the productDO is valid and if yes it saves it
     *
     * @param productDO to be validated and saved
     * @return ResponseDO
     */
    public ResponseDO validateAndSaveProduct(ProductDO productDO) {
        logger.atDebug().log("validateAndSaveProduct in ProductService.");

        if (!validateProduct(productDO)) {
            logger.atDebug().log("The new product to be saved contains invalid data.");
            return new ResponseDO(null, INVALID_DATA);

        } else {
            Product product = saveProduct(productDO);
            if (product != null) {
                return new ResponseDO(productConverter.convertProductEntityToDO(product), SUCCESS);
            }
            return new ResponseDO(null, ERROR);
        }
    }

    /**
     * Method that converts a productDO into a product entity and saved it
     *
     * @param productDO the product to be saved
     * @return Product the saved product
     */
    public Product saveProduct(ProductDO productDO) {
        logger.atDebug().log("checkAndSaveProduct in ProductService.");

        try {
            Product product = productConverter.convertProductDOToEntity(productDO);
            logger.atInfo().log("Save new product");
            return productRepository.save(product);
        } catch (Exception e) {
            logger.atError().addArgument(e).log("Unable to save a new product. Reason: {}");
            return null;
        }
    }

    /**
     * Method that returns all the existing products
     *
     * @return <List<ProductDO>
     */
    public List<ProductDO> getAllProducts() {
        logger.atInfo().log("Get all products.");
        List<ProductDO> productDOList = new ArrayList<>();
        try {
            List<Product> productList = productRepository.findAll();
            if (!CollectionUtils.isEmpty(productList)) {
                productList.forEach(product -> productDOList.add(productConverter.convertProductEntityToDO(product)));
            }
        } catch (Exception e) {
            logger.atError().addArgument(e).log("Unable to get the product list. Reason: {}");
            return null;

        }
        return productDOList;
    }

    /**
     * Method that validates and updates a product quantity
     *
     * @param id the id of the product to be updated
     * @param quantity the updated quantity
     * @return ResponseDO
     */
    public ResponseDO validateAndUpdateProductQuantity(Long id, Integer quantity) {

        if (quantity < 0 && id == null) {
            logger.atDebug().log("The product to be update contains invalid data.");
            return new ResponseDO(null, INVALID_DATA);
        } else {
            try {
                Product product = productRepository.findById(id).map(productToBeUpdated -> {
                    productToBeUpdated.setQuantity(quantity);
                    return productRepository.save(productToBeUpdated);
                }).orElse(null);
                if (product != null) {
                    return new ResponseDO(productConverter.convertProductEntityToDO(product), SUCCESS);
                }
                return new ResponseDO(null, NOT_FOUND);
            } catch (Exception e) {
                logger.atError().addArgument(e).log("Unable to update the product quantity. Reason: {}");
                return new ResponseDO(null, ERROR);
            }
        }
    }

    /**
     * Method that validates and updates a product price
     *
     * @param id the id of the product to be updated
     * @param price the updated price
     * @return ResponseDO
     */
    public ResponseDO validateAndUpdateProductPrice(Long id, Double price) {

        if ((Double.isNaN(price) || price <= 0) && id == null) {
            logger.atDebug().log("The product to be update contains invalid data.");
            return new ResponseDO(null, INVALID_DATA);
        } else {
            try {
                Product product = productRepository.findById(id).map(productToBeUpdated -> {
                    productToBeUpdated.setPrice(price);
                    return productRepository.save(productToBeUpdated);
                }).orElse(null);

                if (product != null) {
                    return new ResponseDO(productConverter.convertProductEntityToDO(product), SUCCESS);
                }
                return new ResponseDO(null, NOT_FOUND);
            } catch (Exception e) {
                logger.atError().addArgument(e).log("Unable to update the product price. Reason: {}");
                return new ResponseDO(null, ERROR);
            }
        }
    }

    /**
     * Method that deletes a product based on the id
     *
     * @param id the id of the product to be deleted
     * @return ResponseDO
     */
    public ResponseDO deleteProduct(Long id) {
        logger.atInfo().log("Delete the product.");
        try {
            if (productRepository.existsById(id)) {
                logger.atDebug().addArgument(id).log("The product with id {} exist and can be deleted.");
                productRepository.deleteById(id);
            }
        } catch (Exception e) {
            logger.atError().addArgument(e).log("Unable to delete the product. Reason: {}");
            return new ResponseDO(null, ERROR);
        }
        return new ResponseDO(null, SUCCESS);
    }

    /**
     * Method to validate a productDO
     *
     * @param productDO the productDO to be validated
     * @return true if the product is valid
     */
    public boolean validateProduct(ProductDO productDO) {
        return !(containsIllegalsCharacters(productDO.getName())
                || containsIllegalsCharacters(productDO.getDescription())
                || (Double.isNaN(productDO.getPrice()) || productDO.getPrice() <= 0)
                || productDO.getQuantity() < 0);
    }

    /**
     * Check if a string contains illegal characters
     *
     * @param stringToBeChecked the string to be checked for illegal characters
     * @return true is the string contains illegal characters
     */
    public boolean containsIllegalsCharacters(String stringToBeChecked) {
        Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\_^-]");
        Matcher matcher = pattern.matcher(stringToBeChecked);
        return matcher.find();
    }
}

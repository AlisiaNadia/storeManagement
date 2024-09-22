package com.shopManagementTool.converter;

import com.shopManagementTool.entity.Product;
import com.shopManagementTool.inputData.ProductDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    Logger logger = LoggerFactory.getLogger(ProductConverter.class);

    /**
     * Converts from a productDO to a product entity
     *
     * @param productDO the productDO to be converted
     * @return Product
     */
    public Product convertProductDOToEntity(ProductDO productDO) {

        logger.atInfo().log("Convert product DO to BE.");
        return new Product(productDO.getId(),
                productDO.getName(),
                productDO.getPrice(),
                productDO.getDescription(),
                productDO.getQuantity());
    }

    /**
     * Converts from a product entity to a productDO
     *
     * @param product the product entity to be converted
     * @return ProductDO
     */
    public ProductDO convertProductEntityToDO(Product product) {
        logger.atInfo().log("Convert product BE to DO.");
        return new ProductDO(product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getQuantity());
    }
}

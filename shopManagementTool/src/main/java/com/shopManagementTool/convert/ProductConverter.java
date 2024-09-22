package com.shopManagementTool.convert;

import com.shopManagementTool.entity.Product;
import com.shopManagementTool.inputData.ProductDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductConverter {
    Logger logger = LoggerFactory.getLogger(ProductConverter.class);

    public Product convertProductDOToBE(ProductDO productDO) {

        logger.atInfo().log("Convert product DO to BE.");
        Product product = new Product();
        product.setName(productDO.getName());
        product.setDescription(productDO.getDescription());
        product.setQuantity(productDO.getQuantity());
        product.setPrice(productDO.getPrice());
        return product;
    }

    public ProductDO convertProductBEToDO(Product product) {
        logger.atInfo().log("Convert product BE to DO.");
        return new ProductDO(product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getQuantity());
    }
}

package com.example.shopManagementTool.convert;

import com.example.shopManagementTool.entity.Product;
import com.example.shopManagementTool.inputData.ProductDO;

public class ProductConverter {

    public Product convertProductDOToBE(ProductDO productDO) {

        Product product = new Product();
        product.setName(productDO.getName());
        product.setDescription(productDO.getDescription());
        product.setQuantity(productDO.getQuantity());
        product.setPrice(productDO.getPrice());
        return product;
    }
}

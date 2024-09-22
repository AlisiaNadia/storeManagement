package com.shopManagementTool.converter;

import com.shopManagementTool.entity.Product;
import com.shopManagementTool.inputData.ProductDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductConverterTest {

    private static final Long ID = 100L;
    private static final String NAME = "MOUSE";
    private static final Double PRICE = 10.0;
    private static final String DESCRIPTION = "Wireless mouse";
    private static final Integer QUANTITY = 20;

    @Autowired
    private ProductConverter productConverter;

    @Test
    void convertFromProductDOToEntityTest() {
        //given
        ProductDO productDO = new ProductDO(ID, NAME, PRICE, DESCRIPTION, QUANTITY);

        //when
        Product product = productConverter.convertProductDOToEntity(productDO);

        //then
        assertThat(product.getId()).isEqualTo(productDO.getId());
        assertThat(product.getName()).isEqualTo(productDO.getName());
        assertThat(product.getPrice()).isEqualTo(productDO.getPrice());
        assertThat(product.getDescription()).isEqualTo(productDO.getDescription());
        assertThat(product.getQuantity()).isEqualTo(productDO.getQuantity());
    }

    @Test
    void convertFromProductEntityToDOTest() {
        Product product = new Product(ID, NAME, PRICE, DESCRIPTION, QUANTITY);

        //when
        ProductDO productDO = productConverter.convertProductEntityToDO(product);

        //then
        assertThat(productDO.getId()).isEqualTo(product.getId());
        assertThat(productDO.getName()).isEqualTo(product.getName());
        assertThat(productDO.getPrice()).isEqualTo(product.getPrice());
        assertThat(productDO.getDescription()).isEqualTo(product.getDescription());
        assertThat(productDO.getQuantity()).isEqualTo(product.getQuantity());
    }
}

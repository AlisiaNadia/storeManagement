package com.shopManagementTool.service;

import com.shopManagementTool.converter.ProductConverter;
import com.shopManagementTool.entity.Product;
import com.shopManagementTool.entity.repository.ProductRepository;
import com.shopManagementTool.inputData.ProductDO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    private static final Long ID = 100L;
    private static final String NAME = "MOUSE";
    private static final Double PRICE = 10.0;
    private static final Double UPDATE_PRICE = 11.0;
    private static final String DESCRIPTION = "Wireless mouse";
    private static final Integer QUANTITY = 20;
    private static final Integer UPDATED_QUANTITY = 10;

    @Mock
    private ProductConverter productConverter;

    @SpyBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void addNewProductTest() {
        //given
        Product product = new Product(ID, NAME, PRICE, DESCRIPTION, QUANTITY);
        ProductDO productDO = new ProductDO(ID, NAME, PRICE, DESCRIPTION, QUANTITY);

        //when
        when(productConverter.convertProductDOToEntity(productDO)).thenReturn(product);
        productService.validateAndSaveProduct(productDO);

        //then
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void updateProductPriceTest() {
        //given
        Product product = new Product(ID, NAME, PRICE, DESCRIPTION, QUANTITY);

        //when
        when(productRepository.findById(ID)).thenReturn(Optional.of(product));
        productService.validateAndUpdateProductPrice(ID, UPDATE_PRICE);

        //then
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void updateProductQuantityTest() {
        //given
        Product product = new Product(ID, NAME, PRICE, DESCRIPTION, QUANTITY);

        //when
        when(productRepository.findById(ID)).thenReturn(Optional.of(product));
        productService.validateAndUpdateProductQuantity(ID, UPDATED_QUANTITY);

        //then
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void deleteProcutTest() {
        //given
        Product product = new Product(ID, NAME, PRICE, DESCRIPTION, QUANTITY);

        //when
        when(productRepository.existsById(ID)).thenReturn(true);
        productService.deleteProduct(ID);

        //then
        verify(productRepository, times(1)).deleteById(any());
    }
}

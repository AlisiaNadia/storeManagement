package com.shopManagementTool.inputData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDO {

    private Long id;
    private String name;
    private Double price;
    private String description;
    private Integer quantity;

}

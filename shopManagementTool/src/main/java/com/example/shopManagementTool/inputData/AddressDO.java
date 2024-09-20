package com.example.shopManagementTool.inputData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDO {

    private Long id;
    private String city;
    private String country;
    private String street;
    private String number;

}

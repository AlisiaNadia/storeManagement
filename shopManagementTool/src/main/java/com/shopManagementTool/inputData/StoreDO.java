package com.shopManagementTool.inputData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreDO {

    private Long id;
    private String name;
    private AddressDO address;
    private String phoneNumber;

}

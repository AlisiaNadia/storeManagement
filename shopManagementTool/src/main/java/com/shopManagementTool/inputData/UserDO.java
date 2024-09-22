package com.shopManagementTool.inputData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDO {

    private Long id;
    private String firstName;
    private String lastName;
    private String role;
    private AddressDO adress;

}
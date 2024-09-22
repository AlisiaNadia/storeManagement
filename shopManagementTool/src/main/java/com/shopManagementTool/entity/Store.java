package com.shopManagementTool.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Address address;

    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;


}

package com.business.Api_Java.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @Column(name = "address", nullable = false, length = 250)
    private String address;

    @Column(name = "number", nullable = false, length = 20)
    private String number;

    @Column(name = "complement", length = 250)
    private String complement;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "city", length = 250)
    private String city;

    @Column(name = "state", length = 250)
    private String state;

    @Column(name = "country", length = 250)
    private String country;
}

package com.business.Api_Java.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "addresses")
@Schema(description = "Represents an Address entity for a client")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the address", example = "1")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_client", nullable = false)
    @NotNull(message = "Client information is required.")
    @Schema(description = "The client associated with the address", required = true)
    private Client client;

    @Column(name = "address", nullable = true, length = 250)
    @Schema(description = "The street address", example = "123 Main Street", required = false)
    private String address;

    @Column(name = "number", nullable = false, length = 20)
    @NotEmpty(message = "O número do endereço é obrigatório.")
    @Schema(description = "The address number", example = "456", required = true)
    private String number;

    @Column(name = "complement", length = 250)
    @Schema(description = "Complementary address information like suite or apartment", required = false)
    private String complement;

    @Column(name = "postal_code", length = 10, nullable = false)
    @NotEmpty(message = "O código postal é obrigatório.")
    @Schema(description = "Postal code for the address", example = "12345-678", required = true)
    private String postalCode;

    @Column(name = "city", length = 250)
    @Schema(description = "City where the address is located", example = "New York", required = false)
    private String city;

    @Column(name = "state", length = 250)
    @Schema(description = "State or province of the address", example = "NY", required = false)
    private String state;

    @Column(name = "country", length = 250)
    @Schema(description = "Country of the address", example = "USA", required = false)
    private String country;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

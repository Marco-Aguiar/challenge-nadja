package com.business.Api_Java.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 250)
    @NotNull(message = "O primeiro nome é obrigatório")
    private String firstName;

    @Column(name = "last_name", length = 250)
    private String lastName;

    @Column(name = "email", nullable = false, length = 250)
    @Email(message = "Email inválido")
    @NotNull(message = "O email é obrigatório")
    private String email;
}

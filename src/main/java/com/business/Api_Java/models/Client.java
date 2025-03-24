package com.business.Api_Java.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "clients")
@Schema(description = "Represents a client with personal information")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the client", example = "1")
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 250)
    @NotNull(message = "O primeiro nome é obrigatório")
    @Schema(description = "The client's first name", example = "John", required = true)
    private String firstName;

    @Column(name = "last_name", length = 250)
    @Schema(description = "The client's last name", example = "Doe")
    private String lastName;

    @Column(name = "email", nullable = false, length = 250)
    @Email(message = "Email inválido")
    @NotNull(message = "O email é obrigatório")
    @Schema(description = "The client's email address", example = "john.doe@example.com", required = true)
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

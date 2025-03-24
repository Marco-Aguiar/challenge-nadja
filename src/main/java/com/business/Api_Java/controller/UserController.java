package com.business.Api_Java.controller;

import com.business.Api_Java.models.Client;
import com.business.Api_Java.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Operations related to Client management")
public class UserController {

    @Autowired
    private ClientService clientService;

    @Operation(summary = "Create a new client", description = "This endpoint creates a new client in the system")
    @ApiResponse(responseCode = "200", description = "Client created successfully")
    @ApiResponse(responseCode = "400", description = "Client information is missing or invalid")
    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody @Parameter(description = "Client details to be created") Client client) {
        if (client.getFirstName() == null || client.getFirstName().isEmpty()) {
            return ResponseEntity.badRequest().body("First name is required.");
        }

        if (client.getEmail() == null || client.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }

        if (!isValidEmail(client.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }

        if (client.getLastName() == null || client.getLastName().isEmpty()) {
            return ResponseEntity.badRequest().body("Last name is required.");
        }

        Client savedClient = clientService.saveClient(client);

        return ResponseEntity.created(URI.create("/clients/" + savedClient.getId())).body(savedClient);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }



    @Operation(summary = "Get all clients", description = "This endpoint returns a list of all clients in the system")
    @ApiResponse(responseCode = "200", description = "List of clients returned successfully")
    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @Operation(summary = "Get client by ID", description = "This endpoint retrieves a client by its ID")
    @ApiResponse(responseCode = "200", description = "Client found successfully")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(
            @PathVariable @Parameter(description = "ID of the client to retrieve") Integer id) {
        Optional<Client> client = clientService.getClientById(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update an existing client", description = "This endpoint updates a client by its ID")
    @ApiResponse(responseCode = "200", description = "Client updated successfully")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(
            @PathVariable @Parameter(description = "ID of the client to update") Integer id,
            @RequestBody @Parameter(description = "Updated client details") Client client) {
        Optional<Client> existingClient = clientService.getClientById(id);
        if (existingClient.isPresent()) {
            client.setId(id);
            Client updatedClient = clientService.saveClient(client);
            return ResponseEntity.ok(updatedClient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a client", description = "This endpoint deletes a client by its ID")
    @ApiResponse(responseCode = "204", description = "Client deleted successfully")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable @Parameter(description = "ID of the client to delete") Integer id) {
        Optional<Client> client = clientService.getClientById(id);
        if (client.isPresent()) {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

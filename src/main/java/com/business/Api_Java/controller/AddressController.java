package com.business.Api_Java.controller;

import com.business.Api_Java.dto.CepApiResponse;
import com.business.Api_Java.models.Address;
import com.business.Api_Java.models.Client;
import com.business.Api_Java.services.AddressService;
import com.business.Api_Java.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
@Tag(name = "Addresses", description = "Operations related to Address management")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ClientService clientService;

    @Operation(summary = "Create a new address", description = "This endpoint creates a new address in the system")
    @ApiResponse(responseCode = "201", description = "Address created successfully")
    @ApiResponse(responseCode = "400", description = "Client information is missing")
    @ApiResponse(responseCode = "404", description = "Client not found")
    @PostMapping
    public ResponseEntity<?> createAddress(@RequestBody @Parameter(description = "Address details to be created") Address address) {
        if (address.getClient() == null || address.getPostalCode() == null || address.getNumber() == null) {
            return ResponseEntity.badRequest().body("Client information is required.");
        }

        boolean clientExists = clientService.doesClientExist(address.getClient().getId());
        if (!clientExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client with the provided ID does not exist.");
        }

        Optional<Client> clientOptional = clientService.getClientById(address.getClient().getId());
        if (!clientOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client with the provided ID does not exist.");
        }

        Client client = clientOptional.get();
        address.setClient(client);

        String cep = address.getPostalCode();
        String url = "https://viacep.com.br/ws/" + cep + "/json";
        CepApiResponse response = restTemplate.getForObject(url, CepApiResponse.class);

        if (response != null) {
            address.setAddress(response.getLogradouro());
            address.setComplement(response.getComplemento());
            address.setCity(response.getLocalidade());
            address.setState(response.getUf());
            address.setPostalCode(response.getCep());
            address.setCountry(response.getUf());
        }

        Address createdAddress = addressService.saveAddress(address);

        return ResponseEntity.created(URI.create("/addresses/" + createdAddress.getId())).body(createdAddress);
    }



    @Operation(summary = "Get all addresses", description = "This endpoint returns a list of all addresses in the system")
    @ApiResponse(responseCode = "200", description = "List of addresses returned successfully")
    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @Operation(summary = "Get address by ID", description = "This endpoint retrieves an address by its ID")
    @ApiResponse(responseCode = "200", description = "Address found successfully")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(
            @PathVariable @Parameter(description = "ID of the address to retrieve") Integer id) {
        Optional<Address> address = addressService.getAddressById(id);
        return address.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update an existing address", description = "This endpoint updates an address by its ID")
    @ApiResponse(responseCode = "200", description = "Address updated successfully")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(
            @PathVariable @Parameter(description = "ID of the address to update") Integer id,
            @RequestBody @Parameter(description = "Updated address details") Address address) {

        Optional<Address> existingAddress = addressService.getAddressById(id);
        if (existingAddress.isPresent()) {
            Address updatedAddress = existingAddress.get();

            updateNonNullFields(updatedAddress, address);

            updatedAddress = addressService.saveAddress(updatedAddress);
            return ResponseEntity.ok(updatedAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Delete an address", description = "This endpoint deletes an address by its ID")
    @ApiResponse(responseCode = "204", description = "Address deleted successfully")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable @Parameter(description = "ID of the address to delete") Integer id) {
        Optional<Address> address = addressService.getAddressById(id);
        if (address.isPresent()) {
            addressService.deleteAddress(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public void updateNonNullFields(Address existingAddress, Address newAddress) {
        if (newAddress.getAddress() != null) existingAddress.setAddress(newAddress.getAddress());
        if (newAddress.getNumber() != null) existingAddress.setNumber(newAddress.getNumber());
        if (newAddress.getComplement() != null) existingAddress.setComplement(newAddress.getComplement());
        if (newAddress.getPostalCode() != null) existingAddress.setPostalCode(newAddress.getPostalCode());
        if (newAddress.getCity() != null) existingAddress.setCity(newAddress.getCity());
        if (newAddress.getState() != null) existingAddress.setState(newAddress.getState());
        if (newAddress.getCountry() != null) existingAddress.setCountry(newAddress.getCountry());
    }
}

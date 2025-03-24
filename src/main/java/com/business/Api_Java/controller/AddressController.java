package com.business.Api_Java.controller;

import com.business.Api_Java.models.Address;
import com.business.Api_Java.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
        Address createdAddress = addressService.saveAddress(address);
        return ResponseEntity.ok(createdAddress);
    }

    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Integer id) {
        Optional<Address> address = addressService.getAddressById(id);
        return address.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Integer id, @RequestBody Address address) {
        Optional<Address> existingAddress = addressService.getAddressById(id);
        if (existingAddress.isPresent()) {
            Address updatedAddress = existingAddress.get();
            updatedAddress.setAddress(address.getAddress());
            updatedAddress.setNumber(address.getNumber());
            updatedAddress.setComplement(address.getComplement());
            updatedAddress.setPostalCode(address.getPostalCode());
            updatedAddress.setCity(address.getCity());
            updatedAddress.setState(address.getState());
            updatedAddress.setCountry(address.getCountry());

            updatedAddress = addressService.saveAddress(updatedAddress);
            return ResponseEntity.ok(updatedAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) {
        Optional<Address> address = addressService.getAddressById(id);
        if (address.isPresent()) {
            addressService.deleteAddress(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

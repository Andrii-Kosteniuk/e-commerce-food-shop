package com.ecommerce.deliveryservice.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DeliveryInfo {

    private String country;
    private String state;
    private String street;
    private String city;
    private String zipCode;
    private String phone;

}

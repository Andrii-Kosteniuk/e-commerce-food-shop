package com.commondto.user;


import com.ecommerce.deliveryservice.model.DeliveryInfo;

public record UserResponse (

        String firstName,
        String lastName,
        String email,
        DeliveryInfo deliveryInfo
) {

}

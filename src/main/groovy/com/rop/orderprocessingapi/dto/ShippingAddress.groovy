package com.rop.orderprocessingapi.dto

import groovy.transform.Canonical

@Canonical
class ShippingAddress {

    String shippingAddress1
    String shippingAddress2
    String city
    String state
    String zipCode
}

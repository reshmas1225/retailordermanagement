package com.rop.orderprocessingapi.dto

import groovy.transform.Canonical

/**
 * Request object for creating order requests.
 */

@Canonical
class OrderItem {
    int itemId
    int itemQuantity
    String itemName
    double price
}

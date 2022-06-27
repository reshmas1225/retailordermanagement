package com.rop.orderprocessingapi.dto

import groovy.transform.Canonical
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Canonical
@Document(collection = "order_documents")
class OrderRequest implements Serializable {
    @Id
    @Field("_id")
    ObjectId id

    @Field("customer_id")
    int customerId

    @Field("order_items")
    List<OrderItem> orderItems

    @Field("shipping_address")
    ShippingAddress shippingAddress

    @Field("status")
    String status
}

package com.rop.orderprocessingapi.repository

import com.rop.orderprocessingapi.dto.OrderRequest
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface OrderRepository extends MongoRepository<OrderRequest, String> {
    @Query("{'_id': ?0}")
    OrderRequest findByObjectId(ObjectId id)

    @Query("{'customer_id': ?0}")
    List<OrderRequest> findOrderByCustId(int customerId)
}

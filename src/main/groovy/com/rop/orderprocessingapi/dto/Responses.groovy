package com.rop.orderprocessingapi.dto

import groovy.transform.Canonical

@Canonical
class Responses<T> {

    int page
    int pageSize
    int totalResults
    List<T> resources
}
